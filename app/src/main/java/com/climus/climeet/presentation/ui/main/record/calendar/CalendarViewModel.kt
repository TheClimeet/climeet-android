package com.climus.climeet.presentation.ui.main.record.calendar

import androidx.browser.trusted.sharing.ShareData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed class CalendarEvent {
}

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<CalendarEvent>()
    val event: SharedFlow<CalendarEvent> = _event.asSharedFlow()

    init {

    }

}