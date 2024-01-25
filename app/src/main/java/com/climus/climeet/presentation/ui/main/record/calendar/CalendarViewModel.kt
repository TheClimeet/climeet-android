package com.climus.climeet.presentation.ui.main.record.calendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

    val currentYear = MutableStateFlow("${YearMonth.now().year}")
    val currentMonth = MutableStateFlow("${YearMonth.now().month.value}")

    val isToday = MutableStateFlow(true)

    init {

    }

    fun updateSelectedYearMonth(selectedYear: String, selectedMonth: String) {
        currentYear.value = selectedYear
        currentMonth.value = selectedMonth
    }

    fun setIsToday(today: Boolean) {
        isToday.value = today
    }

}