package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ClimberProfileShortsBtnState(
    val shorts: Boolean = false,
)

data class ClimberShortsVisibilityState (
    val public: Boolean = true,
    val follow: Boolean = false,
    val closed: Boolean = false,
    val state: String = "전체공개"
)

@HiltViewModel
class MyPageClimberProfileShortsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ClimberShortsVisibilityState())
    val uiState: StateFlow<ClimberShortsVisibilityState> = _uiState.asStateFlow()

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
    * [전체보기] [팔로우만] [나만보기] 버튼 중 하나를 눌렀을 때 해당 버튼으로 공개 범위를 설정
    * 선택지 버튼이 다시 안 보이게 설정
    */
    fun setShortsVisibility(target: Int) {
        _uiState.update { state ->
            when (target) {
                1 -> {
                    state.copy(public = true, follow = false, closed = false, state = "전체공개")
                }
                2 -> {
                    state.copy(public = false, follow = true, closed = false, state = "팔로우만")
                }
                3 -> {
                    state.copy(public = false, follow = false, closed = true, state = "나만보기")
                }
                else -> {
                    state.copy(public = true, follow = false, closed = false, state = "전체공개")
                }
            }
        }

        _btnState.update { state ->
            state.copy(
                shorts = false
            )
        }
    }
}