package com.climus.climeet.presentation.ui.intro.signup.climer.setprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerProfileEvent {
    data object NavigateToBack : SetClimerProfileEvent()
    data object NavigateToSetLevel : SetClimerProfileEvent()
}

@HiltViewModel
class SetClimerProfileViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SetClimerProfileEvent>()
    val event: SharedFlow<SetClimerProfileEvent> = _event.asSharedFlow()

    val nick = MutableStateFlow(ClimerSignupForm.nickName)


    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.NavigateToBack)
        }
    }

    fun navigateToSetLevel() {
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.NavigateToSetLevel)
        }
    }

}