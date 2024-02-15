package com.climus.climeet.presentation.customview.selectdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SelectDateBottomEvent {
    data object CloseFragment : SelectDateBottomEvent()
    data object UpdateIsToday : SelectDateBottomEvent()
    data object SetDate : SelectDateBottomEvent()
}

class SelectDateBottomSheetViewModel : ViewModel() {
    private val _event = MutableSharedFlow<SelectDateBottomEvent>()
    val event: SharedFlow<SelectDateBottomEvent> = _event.asSharedFlow()


    val isToday = MutableStateFlow(false)

    fun closeFragment() {
        viewModelScope.launch {
            _event.emit(SelectDateBottomEvent.CloseFragment)
        }
    }

    fun setDate() {
        viewModelScope.launch {
            _event.emit(SelectDateBottomEvent.SetDate)
        }
    }

    fun setIsTodayToFalse(){
        isToday.update { false }
    }

    fun updateIsToday() {
        if (!isToday.value) {
            viewModelScope.launch {
                _event.emit(SelectDateBottomEvent.UpdateIsToday)
            }
        }

        isToday.update { !isToday.value }
    }

}