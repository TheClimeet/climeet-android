package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.remote.IntroApi
import com.climus.climeet.presentation.ui.intro.login.climer.ClimerLoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerCompleteEvent{

}

@HiltViewModel
class SetClimerCompleteViewModel @Inject constructor(
    private val introApi: IntroApi
) : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerCompleteEvent>()
    val event: SharedFlow<SetClimerCompleteEvent> = _event.asSharedFlow()

    fun signUp(provider: String, accessToken: String, signUpRequest: ClimerSignupRequest) {
        viewModelScope.launch {
            try {
                val response = introApi.signUpClimer(provider, accessToken, signUpRequest)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("signupApiTest", "성공 ${response.code()}")
                    Log.d("signupApiTest", "성공 ${response.message()}")
                } else {
                    Log.d("signupApiTest", "실패 ${response.code()}")
                    Log.d("signupApiTest", "실패 ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("signupApiTest", "$e")
            }
        }
    }


}