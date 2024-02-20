package com.climus.climeet.presentation.ui.main.global.climerprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ClimberProfileUiState(
    val userName: String = "",
    val userProfileImg: String = "",
    val followingString: String = "",
    val isFollower: Boolean = false
)


@HiltViewModel
class ClimberProfileViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClimberProfileUiState())
    val uiState: StateFlow<ClimberProfileUiState> = _uiState.asStateFlow()

    private var userId: Long = 0

    fun setUserId(id: Long) {
        userId = id
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo(userId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                userName = it.body.userName,
                                userProfileImg = it.body.userProfileUrl,
                                followingString = "팔로워 ${it.body.followerCount}  |  팔로잉 ${it.body.followingCount}",
                                isFollower = it.body.isFollower
                            )
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    fun follow() {
        viewModelScope.launch {
            repository.followUser(userId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(isFollower = true)
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    fun unFollow() {
        viewModelScope.launch {
            repository.unfollowUser(userId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(isFollower = false)
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

}