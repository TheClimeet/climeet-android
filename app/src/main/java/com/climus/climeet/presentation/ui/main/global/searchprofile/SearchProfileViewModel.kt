package com.climus.climeet.presentation.ui.main.global.searchprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.FollowCragEvent
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.SearchProfileUiData
import com.climus.climeet.presentation.ui.main.global.toSearchProfileUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchProfileUiState(
    val isGym: Boolean = true,
    val profileList: List<SearchProfileUiData> = emptyList(),
    val progressState: Boolean = false,
    val emptyResultState: Boolean = false,
)

sealed class SearchProfileEvent {
    data class ShowToastMessage(val msg: String) : SearchProfileEvent()
    data class NavigateToGymProfile(val id: Long) : SearchProfileEvent()
    data class NavigateToClimerProfile(val id: Long) : SearchProfileEvent()
}

@HiltViewModel
class SearchProfileViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SearchProfileUiState())
    val uiState: StateFlow<SearchProfileUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchProfileEvent>()
    val event: SharedFlow<SearchProfileEvent> = _event.asSharedFlow()

    private var curJob: Job? = null

    val keyword = MutableStateFlow("")

    init {
        observeKeyword()
    }


    private fun observeKeyword() {
        keyword.onEach {
            if (it.isBlank()) {
                _uiState.update { state ->
                    state.copy(
                        profileList = emptyList(),
                        emptyResultState = false
                    )
                }
            } else {
                curJob?.cancel()

                _uiState.update { state ->
                    state.copy(
                        progressState = true,
                        emptyResultState = false
                    )
                }

                curJob = viewModelScope.launch {
                    delay(500)

                    if (uiState.value.isGym) {
                        repository.getGymListToFollow(it, 0, 15).let { result ->
                            when (result) {
                                is BaseState.Success -> {
                                    if (result.body.result.isNotEmpty()) {
                                        _uiState.update { state ->
                                            state.copy(
                                                profileList = result.body.result.map { item ->
                                                    item.toSearchProfileUiData(
                                                        it,
                                                        follow = ::follow,
                                                        unFollow = ::unfollow,
                                                        navigateToProfile = ::navigateToProfile

                                                    )
                                                },
                                                progressState = false
                                            )
                                        }
                                    } else {
                                        _uiState.update { state ->
                                            state.copy(
                                                profileList = emptyList(),
                                                progressState = false,
                                                emptyResultState = true
                                            )
                                        }
                                    }
                                }

                                is BaseState.Error -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            progressState = false,
                                            emptyResultState = true
                                        )
                                    }

                                }
                            }
                        }
                    } else {
                        repository.getClimberSearchingList(0, 15, it).let { result ->
                            when (result) {
                                is BaseState.Success -> {
                                    if (result.body.result.isNotEmpty()) {
                                        _uiState.update { state ->
                                            state.copy(
                                                profileList = result.body.result.map { item ->
                                                    item.toSearchProfileUiData(
                                                        it,
                                                        follow = ::follow,
                                                        unFollow = ::unfollow,
                                                        navigateToProfile = ::navigateToProfile
                                                    )
                                                },
                                                progressState = false
                                            )
                                        }
                                    } else {
                                        _uiState.update { state ->
                                            state.copy(
                                                profileList = emptyList(),
                                                progressState = false,
                                                emptyResultState = true
                                            )
                                        }
                                    }
                                }

                                is BaseState.Error -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            progressState = false,
                                            emptyResultState = true
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }.launchIn(viewModelScope)
    }


    fun follow(id: Long) {
        viewModelScope.launch {
            if (uiState.value.isGym) {
                repository.followGym(id).let {
                    when (it) {
                        is BaseState.Success -> {

                            val newList = uiState.value.profileList.map{ data ->
                                if(data.id == id){
                                    data.copy(
                                        isFollowing = true
                                    )
                                } else {
                                    data.copy()
                                }
                            }

                            _uiState.update { state ->
                                state.copy(
                                    profileList = newList
                                )
                            }
                        }

                        is BaseState.Error -> {

                        }
                    }
                }
            } else {
                repository.followUser(id).let {
                    when (it) {
                        is BaseState.Success -> {
                            val newList = uiState.value.profileList.map{ data ->
                                if(data.id == id){
                                    data.copy(
                                        isFollowing = true
                                    )
                                } else {
                                    data.copy()
                                }
                            }

                            _uiState.update { state ->
                                state.copy(
                                    profileList = newList
                                )
                            }
                        }

                        is BaseState.Error -> {
                            it.msg // 서버 에러 메시지
                            Log.d("API", it.msg)
                        }
                    }
                }
            }

        }
    }

    private fun unfollow(id: Long) {
        viewModelScope.launch {

            if (uiState.value.isGym) {
                repository.unFollowGym(id).let {
                    when (it) {
                        is BaseState.Success -> {
                            val newList = uiState.value.profileList.map{ data ->
                                if(data.id == id){
                                    data.copy(
                                        isFollowing = false
                                    )
                                } else {
                                    data.copy()
                                }
                            }

                            _uiState.update { state ->
                                state.copy(
                                    profileList = newList
                                )
                            }
                        }

                        is BaseState.Error -> {

                        }
                    }
                }
            } else {
                repository.unfollowUser(id).let {
                    when (it) {
                        is BaseState.Success -> {
                            val newList = uiState.value.profileList.map{ data ->
                                if(data.id == id){
                                    data.copy(
                                        isFollowing = false
                                    )
                                } else {
                                    data.copy()
                                }
                            }

                            _uiState.update { state ->
                                state.copy(
                                    profileList = newList
                                )
                            }
                        }

                        is BaseState.Error -> {
                            it.msg // 서버 에러 메시지
                            Log.d("API", it.msg)
                        }
                    }
                }
            }

        }
    }

    fun changeMode(isGym: Boolean) {
        keyword.value = ""
        _uiState.update { state ->
            state.copy(
                profileList = emptyList(),
                isGym = isGym
            )
        }
    }

    private fun navigateToProfile(id: Long) {
        viewModelScope.launch {
            if (uiState.value.isGym) {
                _event.emit(SearchProfileEvent.NavigateToGymProfile(id))
            } else {
                _event.emit(SearchProfileEvent.NavigateToClimerProfile(id))
            }
        }
    }

    fun deleteKeyword() {
        keyword.value = ""
        _uiState.update { state ->
            state.copy(
                profileList = emptyList(),
            )
        }
    }

}
