package com.climus.climeet.presentation.ui.intro.login.climer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class ClimerLoginEvent {
    data object GoToMainActivity: ClimerLoginEvent()
    data class NavigateToSignUp(val socialType: String, val token: String): ClimerLoginEvent()
}

@HiltViewModel
class ClimerLoginViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<ClimerLoginEvent>()
    val event: SharedFlow<ClimerLoginEvent> = _event.asSharedFlow()

    fun login(type: LoginType, token: String) {

        //todo 
        // - login 서버 통신
        // - 성공시 -> MainActivity 로 이동
        // - 실패시 -> Climer 회원가입 Flow 첫번째인 SetClimerNickFragment 로 이동
    }


}

enum class LoginType(val type: String) {
    KAKAO("KAKAO"),
    NAVER("NAVER")
}