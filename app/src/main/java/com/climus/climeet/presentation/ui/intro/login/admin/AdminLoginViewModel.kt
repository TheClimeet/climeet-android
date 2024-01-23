package com.climus.climeet.presentation.ui.intro.login.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ManagerLoginRequest
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.presentation.util.Constants.X_ACCESS_TOKEN
import com.climus.climeet.presentation.util.Constants.X_MODE
import com.climus.climeet.presentation.util.Constants.X_REFRESH_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class AdminLoginEvent {
    data object NavigateToBack : AdminLoginEvent()
    data object NavigateToFindAccount : AdminLoginEvent()
    data object NavigateToSignUp : AdminLoginEvent()
    data object GoToMainActivity : AdminLoginEvent()
    data class ShowToastMessage(val msg: String) : AdminLoginEvent()
}

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    private val repository: IntroRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<AdminLoginEvent>()
    val event: SharedFlow<AdminLoginEvent> = _event.asSharedFlow()

    val warningText = MutableStateFlow("")

    val id = MutableStateFlow("")
    val pw = MutableStateFlow("")

    init {
        idObserve()
        pwObserve()
    }

    private fun idObserve() {
        id.onEach {
            warningText.value = ""
        }.launchIn(viewModelScope)
    }

    private fun pwObserve() {
        pw.onEach {
            warningText.value = ""
        }.launchIn(viewModelScope)
    }

    fun navigateToFindAccount() {
        viewModelScope.launch {
            _event.emit(AdminLoginEvent.NavigateToFindAccount)
        }
    }

    fun navigateToSignUp() {
        viewModelScope.launch {
            _event.emit(AdminLoginEvent.NavigateToSignUp)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(AdminLoginEvent.NavigateToBack)
        }
    }

    fun login() {

        viewModelScope.launch {
            repository.managerLogin(ManagerLoginRequest(id.value, pw.value)).let{
                when(it){
                    is BaseState.Success -> {
                        
                        // todo X_MODE 는 ADMIN / CLIMER 로 나뉨
                        
                        App.sharedPreferences.edit()
                            .putString(X_ACCESS_TOKEN, it.body.accessToken)
                            .putString(X_REFRESH_TOKEN, it.body.refreshToken)
                            .putString(X_MODE, "ADMIN")
                            .apply()

                        _event.emit(AdminLoginEvent.GoToMainActivity)
                    }

                    is BaseState.Error -> {
                        when(it.code){
                            "MEMBER_002" -> {
                                warningText.value = "계정 정보를 다시 확인해주세요."
                            }
                            else -> {
                                _event.emit(AdminLoginEvent.ShowToastMessage(it.msg))
                            }
                        }
                    }
                }
            }
        }

    }


}