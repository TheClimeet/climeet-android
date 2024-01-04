package com.climus.climeet.presentation.ui.intro.signup.climer

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerLevelEvent {

    data object NavigateToBack : SetClimerLevelEvent()


}

@HiltViewModel
class SetClimerLevelViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerLevelEvent>()
    val event: SharedFlow<SetClimerLevelEvent> = _event.asSharedFlow()

    var explainText = MutableStateFlow("${ClimerSignupForm.nickName}님, 클라이밍을\n시작한지 얼마나 되셨어요?")

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetClimerLevelEvent.NavigateToBack)
        }
    }

}