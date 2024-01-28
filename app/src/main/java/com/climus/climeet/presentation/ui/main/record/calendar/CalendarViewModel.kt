package com.climus.climeet.presentation.ui.main.record.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.record.model.ClimbingRecordData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

sealed class CalendarEvent {
    data object NavigateToCreateClimbingRecord : CalendarEvent()

    data object ShowTimePicker : CalendarEvent()
}

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<CalendarEvent>()
    val event: SharedFlow<CalendarEvent> = _event.asSharedFlow()

    val currentYear = MutableStateFlow("${YearMonth.now().year}")
    val currentMonth = MutableStateFlow("${YearMonth.now().month.value}")

    val isToday = MutableStateFlow(true)
    val isRecordVisible = MutableStateFlow(false)

    val recordList = MutableStateFlow<List<ClimbingRecordData>>(emptyList())

    init {
    }

    fun updateSelectedYearMonth(selectedYear: String, selectedMonth: String) {
        currentYear.value = selectedYear
        currentMonth.value = selectedMonth
    }

    fun setIsToday(today: Boolean) {
        isToday.value = today
    }

    fun setRecord(date: LocalDate) {
        val testDate: LocalDate = LocalDate.of(2024, 1, 13)
        if (date == testDate) {
            isRecordVisible.value = true
            recordList.value = listOf(
                ClimbingRecordData(0, "2024.01.13", "1h 30m", "8", "4", "V5", "더클라임 연남"),
                ClimbingRecordData(1, "2024.01.13", "2h 30m", "5", "10", "V2", "더클라임 강남"),
                ClimbingRecordData(2, "2024.01.13", "3h 30m", "3", "9", "V6", "더클라임 부천")
            )
        } else {
            isRecordVisible.value = false
            recordList.value = emptyList()
        }
    }

    fun navigateToCreateClimbingRecord() {
        viewModelScope.launch {
            _event.emit(CalendarEvent.NavigateToCreateClimbingRecord)
        }
    }

    fun showTimePicker(){
        viewModelScope.launch {
            _event.emit(CalendarEvent.ShowTimePicker)
        }
    }

}