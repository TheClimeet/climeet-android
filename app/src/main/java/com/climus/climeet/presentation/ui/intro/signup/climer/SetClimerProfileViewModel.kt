package com.climus.climeet.presentation.ui.intro.signup.climer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SetClimerProfileEvent {

    data object NavigateToBack : SetClimerProfileEvent()

}

@HiltViewModel
class SetClimerProfileViewModel @Inject constructor() : ViewModel()  {

    private val _event = MutableSharedFlow<SetClimerProfileEvent>()
    val event: SharedFlow<SetClimerProfileEvent> = _event.asSharedFlow()

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(SetClimerProfileEvent.NavigateToBack)
        }
    }

    fun setProfile(){

    }



}