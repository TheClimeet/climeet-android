package com.climus.climeet.presentation.ui.main.record.stats

import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class StatsEvent{

}

@HiltViewModel
class StatsViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<StatsEvent>()
    val event: SharedFlow<StatsEvent> = _event.asSharedFlow()
}