package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.model.MyPageClimberProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ClimberProfileSettingBtnState(
    val homeGym: Boolean = false,
    val avgComplete: Boolean = false,
    val avgCompleteLevel: Boolean = false
)

@HiltViewModel
class MyPageClimberProfileInfoViewModel @Inject constructor() : ViewModel() {
    // todo : 공개 범위 버튼 설정 가져오기 및 변경 사항 서버에 반영

    // 선택한 공개 범위 설정
    private val _uiState = MutableStateFlow(MyPageClimberProfileUiState())
    val uiState: StateFlow<MyPageClimberProfileUiState> = _uiState.asStateFlow()

    // 공개 범위 설정 버튼 관리
    private val _btnState = MutableStateFlow(ClimberProfileSettingBtnState())
    val btnState: StateFlow<ClimberProfileSettingBtnState> = _btnState.asStateFlow()

    // 버튼 클릭 시, 두 선택지 버튼 보이게 설정
    fun showHomeGymBtns() {
        _btnState.update { state ->
            state.copy(
                homeGym = true
            )
        }
    }
    fun showCompleteBtns() {
        _btnState.update { state ->
            state.copy(
                avgComplete = true
            )
        }
    }
    fun showCompleteLevelBtns() {
        _btnState.update { state ->
            state.copy(
                avgCompleteLevel = true
            )
        }
    }

    /*
     * [전체보기] [나만보기] 버튼 중 하나를 눌렀을 때 해당 버튼으로 공개 범위를 설정
     * 두 선택지 버튼이 다시 안 보이게 설정
     */
    fun setHomeGymState(visibility: Boolean) {
        _uiState.update { state ->
            state.copy(
                homeGymPublic = visibility
            )
        }
        _btnState.update { state ->
            state.copy(
                homeGym = false
            )
        }
        Log.d("mypage_edit", "홈짐 : $visibility")
    }
    fun setAvgCompleteState(visibility: Boolean) {
        _uiState.update { state ->
            state.copy(
                avgCompletionRatePublic = visibility
            )
        }
        _btnState.update { state ->
            state.copy(
                avgComplete = false
            )
        }
        Log.d("mypage_edit", "평균 완등률 : $visibility")
    }
    fun setAvgCompleteLevelState(visibility: Boolean) {
        _uiState.update { state ->
            state.copy(
                avgCompletionLevelPublic = visibility
            )
        }
        _btnState.update { state ->
            state.copy(
                avgCompleteLevel = false
            )
        }
        Log.d("mypage_edit", "평균 완등 레벨 : $visibility")
    }
}