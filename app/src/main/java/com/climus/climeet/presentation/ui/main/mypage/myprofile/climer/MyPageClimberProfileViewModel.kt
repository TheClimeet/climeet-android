package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.model.MyPageClimberProfileUiState
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager.ClimberProfileSettingBtnState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageClimberProfileViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    // 공개 범위 관리
    private val _uiState = MutableStateFlow(MyPageClimberProfileUiState())
    val uiState: StateFlow<MyPageClimberProfileUiState> = _uiState.asStateFlow()

    // 공개 범위 설정 버튼 관리
    private val _btnState = MutableStateFlow(ClimberProfileSettingBtnState())
    val btnState: StateFlow<ClimberProfileSettingBtnState> = _btnState.asStateFlow()

    private var climberId: Long = 0

    fun setClimberId(id: Long) {
        climberId = id
        getProfilePrivacy()
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

    private fun editProfilePrivacy() {

    }

    // 정보탭 공개 범위
    fun setPrivacyState(target: String, privacy: Boolean) {
        when (target) {
            "homeGym" -> {
                _uiState.update { state ->
                    state.copy(
                        homeGymPublic = privacy
                    )
                }
            }

            "avgComplete" -> {
                _uiState.update { state ->
                    state.copy(
                        avgCompletionRatePublic = privacy
                    )
                }
            }

            "avgCompleteLevel" -> {
                _uiState.update { state ->
                    state.copy(
                        avgCompletionLevelPublic = privacy
                    )
                }
            }

            else -> {
            }
        }
    }
}