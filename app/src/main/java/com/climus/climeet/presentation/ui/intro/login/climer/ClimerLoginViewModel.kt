package com.climus.climeet.presentation.ui.intro.login.climer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ClimerLoginEvent {
    data object StartNaverLogin : ClimerLoginEvent()
    data object StartKakaoLogin : ClimerLoginEvent()
    data object GoToMainActivity : ClimerLoginEvent()
    data class NavigateToSignUp(val socialType: String, val token: String) : ClimerLoginEvent()
    data object NavigateBack : ClimerLoginEvent()
    data class ShowToastMessage(val msg: String) : ClimerLoginEvent()
}

@HiltViewModel
class ClimerLoginViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<ClimerLoginEvent>()
    val event: SharedFlow<ClimerLoginEvent> = _event.asSharedFlow()

    fun login(type: String, token: String) {

        //todo 
        // - login 서버 통신
        // - 성공시 -> MainActivity 로 이동
        // - 실패시 -> Climer 회원가입 Flow 첫번째인 SetClimerNickFragment 로 이동

        viewModelScope.launch {
            _event.emit(ClimerLoginEvent.NavigateToSignUp(type, token))
        }
    }

    fun navigateBack() {
        Log.d(TAG, "click")
        viewModelScope.launch {
            _event.emit(ClimerLoginEvent.NavigateBack)
        }
    }

    fun startKakaoLogin() {
        viewModelScope.launch {
            _event.emit(ClimerLoginEvent.StartKakaoLogin)
        }
    }

    fun startNaverLogin() {
        viewModelScope.launch {
            _event.emit(ClimerLoginEvent.StartNaverLogin)
        }
    }


}
