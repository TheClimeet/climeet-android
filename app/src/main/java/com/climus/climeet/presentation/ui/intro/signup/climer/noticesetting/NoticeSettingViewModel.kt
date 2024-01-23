package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.intro.login.climer.ClimerLoginEvent
import com.climus.climeet.presentation.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class NoticeSettingEvent{
    data object NavigateToComplete: NoticeSettingEvent()
    data object NavigateToBack: NoticeSettingEvent()
    data class ShowToastMessage(val msg: String): NoticeSettingEvent()
}

@HiltViewModel
class NoticeSettingViewModel @Inject constructor(
    private val repository: IntroRepository
): ViewModel() {

    private val _event = MutableSharedFlow<NoticeSettingEvent>()
    val event: SharedFlow<NoticeSettingEvent> = _event.asSharedFlow()

    fun signUp(
        provider: String,
        accessToken: String,
        signUpRequest: ClimerSignupRequest
    ){
        viewModelScope.launch {
            repository.climerSignUp(provider, accessToken, signUpRequest).let{
                when(it){
                    is BaseState.Success -> {
                        App.sharedPreferences.edit()
                            .putString(Constants.X_ACCESS_TOKEN, it.body.accessToken)
                            .putString(Constants.X_REFRESH_TOKEN, it.body.refreshToken)
                            .putString(Constants.X_MODE, "CLIMER")
                            .apply()

                        _event.emit(NoticeSettingEvent.NavigateToComplete)
                    }

                    is BaseState.Error -> _event.emit(NoticeSettingEvent.ShowToastMessage(it.msg))
                }
            }
        }
    }

}