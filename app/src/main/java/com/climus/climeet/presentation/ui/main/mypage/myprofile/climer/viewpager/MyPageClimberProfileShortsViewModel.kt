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

data class ClimberProfileShortsBtnState(
    val shorts: Boolean = false,
)

@HiltViewModel
class MyPageClimberProfileShortsViewModel @Inject constructor() : ViewModel() {
    // todo : 공개 범위 버튼 설정 가져오기 및 변경 사항 서버에 반영

    // 선택한 공개 범위 설정
    private val _uiState = MutableStateFlow(MyPageClimberProfileUiState())
    val uiState: StateFlow<MyPageClimberProfileUiState> = _uiState.asStateFlow()

    // 공개 범위 설정 버튼 관리
    private val _btnState = MutableStateFlow(ClimberProfileShortsBtnState())
    val btnState: StateFlow<ClimberProfileShortsBtnState> = _btnState.asStateFlow()

    // 버튼 클릭 시, 두 선택지 버튼 보이게 설정
    fun showShortsBtns() {
        _btnState.update { state ->
            state.copy(
                shorts = true
            )
        }
    }

    /*
     * [전체보기] [나만보기] 버튼 중 하나를 눌렀을 때 해당 버튼으로 공개 범위를 설정
     * 두 선택지 버튼이 다시 안 보이게 설정
     */
    fun setShortsState(visibility: Boolean) {
        _uiState.update { state ->
            state.copy(
                shortsPublic = visibility
            )
        }
        _btnState.update { state ->
            state.copy(
                shorts = false
            )
        }
        Log.d("mypage_edit", "숏츠 : $visibility")
    }
}