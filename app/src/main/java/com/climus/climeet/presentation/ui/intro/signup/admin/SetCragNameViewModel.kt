package com.climus.climeet.presentation.ui.intro.signup.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetCragNameEvent {
    data object NavigateToBack : SetCragNameEvent()
    data object NavigateToNext : SetCragNameEvent()
}

@HiltViewModel
class SetCragNameViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetCragNameEvent>()
    val event: SharedFlow<SetCragNameEvent> = _event.asSharedFlow()

    // 배경화면 설정으로 이동
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetCragNameEvent.NavigateToBack)
        }
    }

    // 알림 설정으로 이동
    fun navigateToNext(){
        viewModelScope.launch {
            _event.emit(SetCragNameEvent.NavigateToNext)
        }
    }
}