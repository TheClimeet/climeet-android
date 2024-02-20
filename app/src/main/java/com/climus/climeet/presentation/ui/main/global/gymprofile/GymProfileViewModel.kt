package com.climus.climeet.presentation.ui.main.global.gymprofile

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

data class GymProfileInfoUiState(
    val gymId: Long = 0L,
    val gymProfileImageUrl: String = "",
    val gymBackGroundImageUrl: String = "",
    val gymName: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val averageRating: Float = 0F,
    val reviewCount: Int = 0
)

@HiltViewModel
class GymProfileViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymProfileInfoUiState())
    val uiState: StateFlow<GymProfileInfoUiState> = _uiState.asStateFlow()

    var gymId = MutableLiveData<Long>()

    val followState = MutableStateFlow(false)

    fun getGymProfileInfo() {
        viewModelScope.launch {
            gymId.value?.let {
                repository.getGymProfileTopInfo(it).let { it ->
                    when (it) {
                        is BaseState.Success -> {
                            // 성공
                            _uiState.update { state ->
                                state.copy(
                                    gymId = gymId.value!!,
                                    gymProfileImageUrl = it.body.gymProfileImageUrl,
                                    gymBackGroundImageUrl = it.body.gymBackGroundImageUrl,
                                    gymName = it.body.gymName,
                                    followerCount = it.body.followerCount,
                                    followingCount = it.body.followingCount,
                                    averageRating = it.body.averageRating,
                                    reviewCount = it.body.reviewCount
                                )
                            }
                        }

                        is BaseState.Error -> {
                            it.msg // 서버 에러 메시지
                            Log.d("gym_profile", "상단 정보 불러오기 실패")
                        }
                    }
                }
            }
        }
    }
}