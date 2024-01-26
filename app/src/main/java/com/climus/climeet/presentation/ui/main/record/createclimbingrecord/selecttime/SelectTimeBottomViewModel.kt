package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class SelectTimeBottomEvent {

}

@HiltViewModel
class SelectTimeBottomViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<SelectTimeBottomEvent>()
    val event: SharedFlow<SelectTimeBottomEvent> = _event.asSharedFlow()


}