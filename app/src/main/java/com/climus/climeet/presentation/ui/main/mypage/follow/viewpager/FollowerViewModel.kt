package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.SearchProfileUiData
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowerUiData
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData
import com.climus.climeet.presentation.ui.main.global.toUserFollowerUiData
import com.climus.climeet.presentation.ui.main.global.toUserFollowingUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowingUiData
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

data class FollowerUiState(
    val isGym: Boolean = true,
    val profileList: List<SearchProfileUiData> = emptyList(),
    val gymFollowerList: List<FollowUiData> = emptyList(),
    val climberFollowerList: List<FollowUiData> = emptyList(),
)

sealed class FollowerEvent {
    data class NavigateToClimerProfile(val userId: Long) : FollowerEvent()
    data class NavigateToGymProfile(val gymId: Long) : FollowerEvent()
}

@HiltViewModel
class FollowerViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _uiState = MutableStateFlow(FollowerUiState())
    val uiState: StateFlow<FollowerUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FollowerEvent>()
    val event: SharedFlow<FollowerEvent> = _event.asSharedFlow()

    fun getUserFollowers(userCategory: String) {
        viewModelScope.launch {
            repository.getUserFollowers(null, userCategory).let {
                when (it) {
                    is BaseState.Success -> {

                        if (userCategory == "Climber") {
                            _uiState.update { state ->
                                state.copy(
                                    climberFollowerList = it.body.map { item ->
                                        item.toUserFollowerUiData(
                                            follow = ::follow,
                                            unFollow = ::unfollow,
                                            navigateToProfile = ::navigateToProfile
                                        )
                                    },
                                )
                            }
                        } else {
                            _uiState.update { state ->
                                state.copy(
                                    gymFollowerList = it.body.map { item ->
                                        item.toUserFollowerUiData(
                                            follow = ::follow,
                                            unFollow = ::unfollow,
                                            navigateToProfile = ::navigateToProfile
                                        )
                                    },
                                )
                            }
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

    private fun unfollow(id: Long) {
        viewModelScope.launch {
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

    private fun navigateToProfile(id: Long) {
        viewModelScope.launch {
            if (uiState.value.isGym) {
                _event.emit(FollowerEvent.NavigateToGymProfile(id))
            } else {
                _event.emit(FollowerEvent.NavigateToClimerProfile(id))
            }
        }
    }
}