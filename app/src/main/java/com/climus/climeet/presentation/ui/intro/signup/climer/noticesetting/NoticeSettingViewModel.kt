package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.repository.AuthRepository
import com.climus.climeet.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class NoticeSettingEvent {
    data object NavigateToComplete : NoticeSettingEvent()
    data object NavigateToBack : NoticeSettingEvent()
    data class ShowToastMessage(val msg: String) : NoticeSettingEvent()
}

@HiltViewModel
class NoticeSettingViewModel @Inject constructor(
    private val repository: IntroRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<NoticeSettingEvent>()
    val event: SharedFlow<NoticeSettingEvent> = _event.asSharedFlow()

    fun signUp(
        provider: String,
        accessToken: String,
        signUpRequest: ClimerSignupRequest
    ) {
        viewModelScope.launch {
            repository.climerSignUp(provider, accessToken, signUpRequest).let {
                when (it) {
                    is BaseState.Success -> {
                        authRepository.putAccessToken(it.body.accessToken)
                        authRepository.putRefreshToken(it.body.refreshToken)
                        authRepository.putLoginMode("ADMIN")

                        _event.emit(NoticeSettingEvent.NavigateToComplete)
                    }

                    is BaseState.Error -> _event.emit(NoticeSettingEvent.ShowToastMessage(it.msg))
                }
            }
        }
    }

}