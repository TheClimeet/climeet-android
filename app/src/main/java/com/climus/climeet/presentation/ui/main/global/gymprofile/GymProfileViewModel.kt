package com.climus.climeet.presentation.ui.main.global.gymprofile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.AuthRepository
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.MyPageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GymProfileInfoUiState(
    val gymId: Long = 0L,
    val gymProfileImageUrl: String = "",
    val gymBackGroundImageUrl: String = "",
    val gymName: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val averageRating: Float = 0F,
    val reviewCount: Int = 0,
)

@HiltViewModel
class GymProfileViewModel @Inject constructor(
    private val repository: MainRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymProfileInfoUiState())
    val uiState: StateFlow<GymProfileInfoUiState> = _uiState.asStateFlow()

    var gymId = MutableLiveData<Long>()

    val followState = MutableStateFlow(false)

    var isModeClimer = MutableStateFlow(true)

    init {
        getLoginMode()
    }

    private fun getLoginMode() {
        viewModelScope.launch {
            isModeClimer.value = authRepository.getLoginMode() == "CLIMER"
        }
    }

    fun setGymId(id: Long) {
        viewModelScope.launch {
            gymId.value = id
        }
        getGymProfileInfo(id)
    }

    fun getGymProfileInfo(id: Long) {
        Log.d("gymIdTest", "viewModel : $id")
        viewModelScope.launch {
            repository.getGymProfileTopInfo(id).let { result ->
                when (result) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                gymId = gymId.value!!,
                                gymProfileImageUrl = result.body.gymProfileImageUrl,
                                gymBackGroundImageUrl = result.body.gymBackGroundImageUrl,
                                gymName = result.body.gymName,
                                followerCount = result.body.followerCount,
                                followingCount = result.body.followingCount,
                                averageRating = result.body.averageRating,
                                reviewCount = result.body.reviewCount
                            )
                        }
                        followState.value = result.body.isFollower
                    }

                    is BaseState.Error -> {
                        result.msg
                        Log.d("gym_profile", "상단 정보 불러오기 실패")
                    }
                }
            }
        }
    }

    fun toggleFollowState() {
        followState.value = !followState.value
        viewModelScope.launch {
            gymId.value?.let { id ->
                viewModelScope.launch {
                    if (followState.value) {
                        repository.followGym(id)
                        _uiState.update { state ->
                            state.copy(
                                followerCount = _uiState.value.followerCount + 1
                            )
                        }
                    } else {
                        repository.unFollowGym(id)
                        _uiState.update { state ->
                            state.copy(
                                followerCount = _uiState.value.followerCount - 1
                            )
                        }
                    }
                }
            } ?: Log.d("gym_profile", "암장 아이디가 설정되지 않았습니다.")
        }
    }

}