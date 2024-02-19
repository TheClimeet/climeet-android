package com.climus.climeet.presentation.ui.main.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RecordEvent{
    data object ShowCalendarFragment : RecordEvent()
    data object ShowStatsFragment : RecordEvent()
}

class RecordViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<RecordEvent>()
    val event: SharedFlow<RecordEvent> = _event.asSharedFlow()

    fun showCalendarFragment(){
        viewModelScope.launch {
            _event.emit(RecordEvent.ShowCalendarFragment)
        }
    }

    fun showStatsFragment(){
        viewModelScope.launch {
            _event.emit(RecordEvent.ShowStatsFragment)
        }
    }

}