package com.climus.climeet.presentation.ui.intro.login.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AdminLoginViewModel @Inject constructor() : ViewModel() {

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
        //todo
        // - login 서버 통신
        // - 성공시 -> MainActivity 로 이동
        // - 실패시 -> Admin 회원가입 Flow 첫번째인 SetCragNameFragment 로 이동
        warningText.value = "계정 정보를 다시 확인해주세요."
    }


}