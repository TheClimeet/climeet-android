package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SelectDateBottomEvent {
    data object CloseFragment : SelectDateBottomEvent()
    data object UpdateIsToday : SelectDateBottomEvent()
    data object SetDate : SelectDateBottomEvent()
}

@HiltViewModel
class SelectDateBottomSheetViewModel @Inject constructor() : ViewModel() {
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

    fun updateIsToday() {
        if (!isToday.value) {
            viewModelScope.launch {
                _event.emit(SelectDateBottomEvent.UpdateIsToday)
            }
        }

        isToday.value = !isToday.value
    }

}