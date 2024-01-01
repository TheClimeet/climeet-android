package com.climus.climeet.presentation.ui.intro.login.climer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClimerLoginViewModel @Inject constructor() : ViewModel() {

    fun login(type: LoginType, token: String) {

        // todo 서버에 로그인 통신
    }

}

enum class LoginType(val type: String) {
    KAKAO("KAKAO"),
    NAVER("NAVER")
}