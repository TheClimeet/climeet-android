package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.model.MyPageClimberProfileUiState
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

sealed class MyPageClimberProfileEvent {
    data object NavigateToEditClimberProfile : MyPageClimberProfileEvent()
}

@HiltViewModel
class MyPageClimberProfileViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    // 공개 범위 관리
    private val _uiState = MutableStateFlow(MyPageClimberProfileUiState())
    val uiState: StateFlow<MyPageClimberProfileUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageClimberProfileEvent>()
    val event: SharedFlow<MyPageClimberProfileEvent> = _event.asSharedFlow()

    private var climberId: Long = 0

    fun setClimberId(id: Long) {
        climberId = id
        getProfilePrivacy()
    }

    fun getClimberId(): Long {
        return climberId
    }

    private fun getProfilePrivacy() {
        viewModelScope.launch {
            repository.getClimberProfilePrivacyState(climberId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                homeGymPublic = it.body.homeGymPublic,
                                avgCompletionLevelPublic = it.body.averageCompletionLevelPublic,
                                avgCompletionRatePublic = it.body.averageCompletionRatePublic
                            )
                        }
                        Log.d(
                            "mypage_climber",
                            "받아온 공개 범위 : home = ${uiState.value.homeGymPublic}, avgComplete = ${uiState.value.avgCompletionRatePublic}, avgLevel = ${uiState.value.avgCompletionLevelPublic}"
                        )
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    // 정보탭 공개 범위 수정 반영
    fun setPrivacyState(target: String, privacy: Boolean) {
        when (target) {
            "homeGym" -> {
                _uiState.update { state ->
                    state.copy(
                        homeGymPublic = privacy
                    )
                }

                viewModelScope.launch {
                    repository.editHomeGymPrivacy().let {
                        when (it) {
                            is BaseState.Success -> {
                                Log.d("mypage_climber", "홈짐 공개범위 수정")
                            }

                            is BaseState.Error -> {

                            }
                        }
                    }
                }
            }

            "avgComplete" -> {
                _uiState.update { state ->
                    state.copy(
                        avgCompletionRatePublic = privacy
                    )
                }

                viewModelScope.launch {
                    repository.editAvgCompletePrivacy().let {
                        when (it) {
                            is BaseState.Success -> {
                                Log.d("mypage_climber", "평균 완등률 공개범위 수정")
                            }

                            is BaseState.Error -> {

                            }
                        }
                    }
                }
            }

            "avgCompleteLevel" -> {
                _uiState.update { state ->
                    state.copy(
                        avgCompletionLevelPublic = privacy
                    )
                }

                viewModelScope.launch {
                    repository.editAvgCompleteLevelPrivacy().let {
                        when (it) {
                            is BaseState.Success -> {
                                Log.d("mypage_climber", "평균 완등 레벨 공개범위 수정")
                            }

                            is BaseState.Error -> {

                            }
                        }
                    }
                }
            }

            else -> {
            }
        }
    }

    fun navigateToEditPage() {
        viewModelScope.launch {
            _event.emit(MyPageClimberProfileEvent.NavigateToEditClimberProfile)
        }
    }
}