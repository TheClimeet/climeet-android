package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class ClimberProfileSettingBtnState(
    val homeGym: Boolean = false,
    val avgComplete: Boolean = false,
    val avgCompleteLevel: Boolean = false
)

sealed class MyPageClimberProfileEvent {
    data class ChangePrivacyState(val target: String, val state: Boolean) : MyPageClimberProfileEvent()
    data object ShowPopupWindow : MyPageClimberProfileEvent()
}

@HiltViewModel
class MyPageClimberProfileInfoViewModel @Inject constructor() : ViewModel() {

    // 공개 범위 설정 버튼 visibility 관리
    private val _btnState = MutableStateFlow(ClimberProfileSettingBtnState())
    val btnState: StateFlow<ClimberProfileSettingBtnState> = _btnState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageClimberProfileEvent>()
    val event: SharedFlow<MyPageClimberProfileEvent> = _event.asSharedFlow()

    // 버튼 클릭 시, 선택지 버튼 보이게 설정
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
    fun setHomeGymState(privacy: Boolean) {
        _btnState.update { state ->
            state.copy(
                homeGym = false
            )
        }
        viewModelScope.launch {
            _event.emit(MyPageClimberProfileEvent.ChangePrivacyState("homeGym", privacy))
        }

        Log.d("mypage_climber", "홈짐 : $privacy")
    }

    fun setAvgCompleteState(privacy: Boolean) {
        _btnState.update { state ->
            state.copy(
                avgComplete = false
            )
        }
        viewModelScope.launch {
            _event.emit(MyPageClimberProfileEvent.ChangePrivacyState("avgComplete", privacy))
        }

        Log.d("mypage_climber", "평균 완등률 : $privacy")
    }

    fun setAvgCompleteLevelState(privacy: Boolean) {
        _btnState.update { state ->
            state.copy(
                avgCompleteLevel = false
            )
        }
        viewModelScope.launch {
            _event.emit(MyPageClimberProfileEvent.ChangePrivacyState("avgCompleteLevel", privacy))
        }

        Log.d("mypage_climber", "평균 완등 레벨 : $privacy")
    }

    fun showPopupWindow() {
        viewModelScope.launch {
            _event.emit(MyPageClimberProfileEvent.ShowPopupWindow)
        }
    }
}