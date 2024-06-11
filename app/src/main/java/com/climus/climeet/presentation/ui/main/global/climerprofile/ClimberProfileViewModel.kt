package com.climus.climeet.presentation.ui.main.global.climerprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isFollower = MutableLiveData<Boolean>()
    val isFollower: LiveData<Boolean> = _isFollower

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
                        _isFollower.postValue(
                            it.body.isFollower
                        )
                        _uiState.update { state ->
                            state.copy(
                                userName = it.body.userName,
                                userProfileImg = it.body.userProfileUrl,
                                followingString = "팔로워 ${it.body.followerCount}  |  팔로잉 ${it.body.followingCount}",
                                followerCount = it.body.followerCount
                            )
                        }
                        followingCount = it.body.followingCount
                    }

                    is BaseState.Error -> {
                        it.msg
                    }
                }
            }
        }
    }

    // todo api 수정되면 쓸모없는거 싸그리 삭제
    fun toggleFollowState() {
        viewModelScope.launch {
            viewModelScope.launch {
                Log.d("follow_test", _isFollower.toString())
                Log.d("follow_test", userId.toString())
                if (isFollower.value == false) {
                    repository.followUser(userId).let { result ->
                        when (result) {
                            is BaseState.Success -> {
                                _isFollower.postValue(true)
                                _uiState.update { state ->
                                    state.copy(
                                        followerCount = _uiState.value.followerCount + 1,
                                        followingString = "팔로워 ${_uiState.value.followerCount + 1}  |  팔로잉 $followingCount",
                                    )
                                }
                                Log.d("follow_test", "${result}")
                            }

                            is BaseState.Error -> {
                                Log.d("follow_test", "${result}")
                            }

                        }
                    }
                } else {
                    repository.unfollowUser(userId).let { result ->
                        when (result) {
                            is BaseState.Success -> {
                                _isFollower.postValue(false)
                                _uiState.update { state ->
                                    state.copy(
                                        followerCount = _uiState.value.followerCount - 1,
                                        followingString = "팔로워 ${_uiState.value.followerCount - 1}  |  팔로잉 $followingCount",
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
}