package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SelectTimeBottomEvent {
    data object CloseFragment : SelectTimeBottomEvent()
}

@HiltViewModel
class SelectTimeBottomViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<SelectTimeBottomEvent>()
    val event: SharedFlow<SelectTimeBottomEvent> = _event.asSharedFlow()

    fun closeFragment() {
        viewModelScope.launch {
            _event.emit(SelectTimeBottomEvent.CloseFragment)
        }
    }

}