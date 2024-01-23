package com.climus.climeet.presentation.ui.intro.login.climer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.util.Constants
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
class ClimerLoginViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<ClimerLoginEvent>()
    val event: SharedFlow<ClimerLoginEvent> = _event.asSharedFlow()

    fun login(type: String, token: String) {

        //todo 
        // - login 서버 통신
        // - 성공시 -> MainActivity 로 이동
        // - 실패시 -> Climer 회원가입 Flow 첫번째인 SetClimerNickFragment 로 이동

        viewModelScope.launch {
            repository.climerLogin(type, token).let{
                when(it){
                    is BaseState.Success -> {

                        App.sharedPreferences.edit()
                            .putString(Constants.X_ACCESS_TOKEN, it.body.accessToken)
                            .putString(Constants.X_REFRESH_TOKEN, it.body.refreshToken)
                            .putString(Constants.X_MODE, "CLIMBER")
                            .apply()

                        _event.emit(ClimerLoginEvent.GoToMainActivity)
                    }

                    is BaseState.Error -> {


                        _event.emit(ClimerLoginEvent.NavigateToSignUp(type, token))
                    }
                }
            }

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
