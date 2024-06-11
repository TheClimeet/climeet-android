package com.climus.climeet.presentation.ui.main.global.climerprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
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


data class ClimberProfileUiState(
    val userName: String = "",
    val userProfileImg: String = "",
    val followingString: String = "",
    val isFollower: Boolean = false,
    val followerCount: Int = 0,
)

sealed class ClimberProfileEvent {
    data class ChangeFollowing(val state: Boolean) : ClimberProfileEvent()
}

@HiltViewModel
class ClimberProfileViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClimberProfileUiState())
    val uiState: StateFlow<ClimberProfileUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ClimberProfileEvent>()
    val event: SharedFlow<ClimberProfileEvent> = _event.asSharedFlow()

    private var userId: Long = 0
    private var followingCount = 0

    fun setUserId(id: Long) {
        userId = id
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo(userId).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("follow_test", it.body.toString())
                        _uiState.update { state ->
                            state.copy(
                                userName = it.body.userName,
                                userProfileImg = it.body.userProfileUrl,
                                followingString = "팔로워 ${it.body.followerCount}  |  팔로잉 ${it.body.followingCount}",
                                isFollower = it.body.isFollower,
                                followerCount = it.body.followerCount
                            )
                        }
                        followingCount = it.body.followingCount
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    fun toggleFollowState() {
        viewModelScope.launch {
            viewModelScope.launch {
                Log.d("follow_test", _uiState.value.isFollower.toString())
                Log.d("follow_test", userId.toString())
                if (!_uiState.value.isFollower) {
                    val result = repository.followUser(userId)
                    when (result) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    followerCount = _uiState.value.followerCount + 1,
                                    followingString = "팔로워 ${_uiState.value.followerCount + 1}  |  팔로잉 $followingCount",
                                    isFollower = true
                                )
                            }
                            Log.d("follow_test", "${result}")
                        }

                        is BaseState.Error -> {
                            Log.d("follow_test", "${result}")
                        }

                    }

                } else {
                    val result = repository.unfollowUser(userId)
                    when (result) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    followerCount = _uiState.value.followerCount - 1,
                                    followingString = "팔로워 ${_uiState.value.followerCount - 1}  |  팔로잉 $followingCount",
                                    isFollower = false
                                )
                            }
                            Log.d("follow_test", "${result}")
                        }

                        is BaseState.Error -> {
                            Log.d("follow_test", "${result.msg}")
                        }
                    }

                }
            }
        }
    }
}