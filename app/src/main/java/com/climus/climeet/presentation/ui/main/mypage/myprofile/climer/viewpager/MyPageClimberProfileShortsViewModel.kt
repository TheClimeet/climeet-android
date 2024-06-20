package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.response.UserShortsVisibilityType
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

data class ClimberProfileShortsBtnState(
    val shorts: Boolean = false,
)

data class ClimberShortsVisibilityState(
    val public: Boolean = true,
    val follow: Boolean = false,
    val closed: Boolean = false,
    val state: String = "전체공개",
    val nowState: UserShortsVisibilityType = UserShortsVisibilityType.PUBLIC,
)

sealed class MyPageClimberProfileShortsEvent {
    data class SetShortsVisibility(val state: UserShortsVisibilityType) :
        MyPageClimberProfileShortsEvent()
}

@HiltViewModel
class MyPageClimberProfileShortsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ClimberShortsVisibilityState())
    val uiState: StateFlow<ClimberShortsVisibilityState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPageClimberProfileShortsEvent>()
    val event: SharedFlow<MyPageClimberProfileShortsEvent> = _event.asSharedFlow()

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
    fun setShortsVisibility(type: Int) {

        _uiState.update { state ->
            when (type) {
                1 -> {
                    state.copy(
                        public = true, follow = false, closed = false, state = "전체공개",
                        nowState = UserShortsVisibilityType.PUBLIC
                    )
                }

                2 -> {
                    state.copy(
                        public = false, follow = true, closed = false, state = "팔로우만",
                        nowState = UserShortsVisibilityType.FOLLOWERS_ONLY
                    )
                }

                3 -> {
                    state.copy(
                        public = false, follow = false, closed = true, state = "나만보기",
                        nowState = UserShortsVisibilityType.PRIVATE
                    )
                }

                else -> {
                    state.copy(
                        public = true, follow = false, closed = false, state = "전체공개",
                        nowState = UserShortsVisibilityType.PUBLIC
                    )
                }
            }
        }

        _btnState.update { state ->
            state.copy(
                shorts = false
            )
        }

        // 숏츠 다시 불러오기
        viewModelScope.launch {
            _event.emit(MyPageClimberProfileShortsEvent.SetShortsVisibility(uiState.value.nowState))
            Log.d("shorts", "숏츠 다시 가져오기 : $uiState.value.nowState")
        }
    }
}