package com.climus.climeet.presentation.ui.intro.signup.admin.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.personal.SetAdminPersonalEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class SetAdminCompleteEvent {
    data object NavigateToBack : SetAdminCompleteEvent()
    data object NavigateToNext : SetAdminCompleteEvent()
}

@HiltViewModel
class SetAdminCompleteViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetAdminCompleteEvent>()
    val event: SharedFlow<SetAdminCompleteEvent> = _event.asSharedFlow()

    // 알림 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetAdminCompleteEvent.NavigateToBack)
        }
    }

    // 로그인 화면으로 이동
    fun navigateToNext(){

        // todo : AdminSignupForm 데이터 API에 넘겨주기

        viewModelScope.launch {
            _event.emit(SetAdminCompleteEvent.NavigateToNext)
        }
    }
}