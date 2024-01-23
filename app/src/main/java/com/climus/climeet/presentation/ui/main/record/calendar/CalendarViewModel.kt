package com.climus.climeet.presentation.ui.main.record.calendar

import androidx.browser.trusted.sharing.ShareData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.main.record.model.CalendarYearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.YearMonth
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