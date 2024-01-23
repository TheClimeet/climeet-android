package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.repository.IntroRepository
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
            repository.climerAuth(provider, accessToken, signUpRequest).let {
                when (it) {
                    is BaseState.Success -> {

                    }

                    is BaseState.Error -> {

                    }
                }
            }

        }
    }

}