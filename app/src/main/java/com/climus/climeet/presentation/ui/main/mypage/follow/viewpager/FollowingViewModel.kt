package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.SearchProfileUiData
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData
import com.climus.climeet.presentation.ui.main.global.toUserFollowingUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowingUiState(
    val isGym: Boolean = true,
    val profileList: List<SearchProfileUiData> = emptyList(),
    val homegymList: List<UserFollowingUiData> = emptyList(),
    val followingList: List<UserFollowingUiData> = emptyList(),
)

sealed class FollowingEvent {
    data class NavigateToGymProfile(val id: Long) : FollowingEvent()
    data class NavigateToClimerProfile(val id: Long) : FollowingEvent()

}

@HiltViewModel
class FollowingViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(FollowingUiState())
    val uiState: StateFlow<FollowingUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FollowingEvent>()
    val event: SharedFlow<FollowingEvent> = _event.asSharedFlow()

    fun getClimberFollowing() {
        viewModelScope.launch {
            repository.getClimberFollowing().let {
                when(it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                followingList = it.body.map { item ->
                                    item.toUserFollowingUiData(
                                        follow = ::follow,
                                        unFollow = ::unfollow,
                                        navigateToProfile = ::navigateToProfile
                                    )
                                },
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

    fun getHomeGyms() {
        viewModelScope.launch {
            repository.getHomeGyms().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                homegymList = it.body.map { item ->
                                    item.toUserFollowingUiData(
                                        follow = ::follow,
                                        unFollow = ::unfollow,
                                        navigateToProfile = ::navigateToProfile
                                    )
                                },
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

    fun changeMode(isGym: Boolean) {
        _uiState.update { state ->
            state.copy(
                isGym = isGym,
            )
        }
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

    private fun navigateToProfile(id: Long) {
        viewModelScope.launch {
            if (uiState.value.isGym) {
                _event.emit(FollowingEvent.NavigateToGymProfile(id))
            } else {
                _event.emit(FollowingEvent.NavigateToClimerProfile(id))
            }
        }
    }
}