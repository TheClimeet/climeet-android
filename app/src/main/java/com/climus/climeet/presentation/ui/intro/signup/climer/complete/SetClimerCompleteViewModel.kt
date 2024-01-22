package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.IntroApi
import com.climus.climeet.data.repository.GlobalRepository
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.ui.intro.login.climer.ClimerLoginEvent
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerCompleteEvent {

}

@HiltViewModel
class SetClimerCompleteViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerCompleteEvent>()
    val event: SharedFlow<SetClimerCompleteEvent> = _event.asSharedFlow()

    fun signUp(provider: String, accessToken: String, signUpRequest: ClimerSignupRequest) {
        viewModelScope.launch {
            when (val result =
                runRemote { repository.climerSignUp(provider, accessToken, signUpRequest) }) {
                is BaseState.Error -> {
                    Log.d(TAG, "error : ${result.code}")
                }
                is BaseState.Success -> {
                    Log.d(TAG, "성공 : ${result.body}")
                    // todo 토큰의 처리
                }
            }


        }
    }


}