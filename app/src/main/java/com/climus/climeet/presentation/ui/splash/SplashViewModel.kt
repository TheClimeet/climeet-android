package com.climus.climeet.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.AuthRepository
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashEvent {
    data object NavigateToMainActivity : SplashEvent()
    data object NavigateToIntroActivity : SplashEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<SplashEvent>()
    val event: SharedFlow<SplashEvent> = _event.asSharedFlow()

    fun checkLoginType() {
        viewModelScope.launch {
            authRepository.getRefreshToken()?.let {
                refreshToken(it)
            } ?: run {
                _event.emit(SplashEvent.NavigateToIntroActivity)
            }
        }
    }

    private fun refreshToken(token: String) {
        viewModelScope.launch {
            authRepository.refreshToken(token).let {
                when (it) {
                    is BaseState.Success -> {
                        authRepository.putAccessToken(it.body.accessToken)
                        authRepository.putRefreshToken(it.body.refreshToken)
                        _event.emit(SplashEvent.NavigateToMainActivity)
                    }

                    is BaseState.Error -> {
                        authRepository.deleteAccessToken()
                        authRepository.deleteRefreshToken()
                        _event.emit(SplashEvent.NavigateToIntroActivity)
                    }
                }
            }
        }
    }

}