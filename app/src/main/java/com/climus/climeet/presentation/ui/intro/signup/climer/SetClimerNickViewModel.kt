package com.climus.climeet.presentation.ui.intro.signup.climer

import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.intro.login.admin.AdminLoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class SetClimerNickEvent {

}

@HiltViewModel
class SetClimerNickViewModel @Inject constructor() : ViewModel(){

    private val _event = MutableSharedFlow<SetClimerNickEvent>()
    val event: SharedFlow<SetClimerNickEvent> = _event.asSharedFlow()

    val warningText = MutableStateFlow("")

    val nick = MutableStateFlow("")

}