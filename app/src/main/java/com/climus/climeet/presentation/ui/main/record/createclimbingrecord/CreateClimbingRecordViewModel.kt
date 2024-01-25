package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class CreateClimbingRecordEvent {

}

@HiltViewModel
class CreateClimbingRecordViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<CreateClimbingRecordEvent>()
    val event: SharedFlow<CreateClimbingRecordEvent> = _event.asSharedFlow()

    init {

    }

}