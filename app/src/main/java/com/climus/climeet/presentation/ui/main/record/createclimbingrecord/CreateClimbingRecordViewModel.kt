package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import javax.inject.Inject

sealed class CreateClimbingRecordEvent {
    data object ShowDatePicker : CreateClimbingRecordEvent()
    data object ShowTimePicker : CreateClimbingRecordEvent()

    data object NavigateToSelectCrag : CreateClimbingRecordEvent()
}

@HiltViewModel
class CreateClimbingRecordViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<CreateClimbingRecordEvent>()
    val event: SharedFlow<CreateClimbingRecordEvent> = _event.asSharedFlow()

    val initDate = CreateRecordData.selectedDate
    val datePickText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일 (${initDate.dayOfWeek})")
    val selectedDate = MutableLiveData<LocalDate>(initDate)

    val timePickText = MutableStateFlow("시간을 입력해주세요 (선택)")
    val selectedStartTime = MutableLiveData(CreateRecordData.selectedStartTime)
    val selectedEndTime = MutableLiveData(CreateRecordData.selectedEndTime)

    val isSelectedCrag = MutableStateFlow(false)
    val selectedCragEvent = MutableLiveData<Pair<Long, String>>()

    init {
        selectCrag(-1, "클라이밍 암장을 선택해주세요")
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun setSelectedTime(start: LocalTime, end: LocalTime) {
        selectedStartTime.value = start
        selectedEndTime.value = end
    }

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.ShowDatePicker)
        }
    }

    fun showTimePicker() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.ShowTimePicker)
        }
    }

    fun setDate() {
        val date = selectedDate.value
        viewModelScope.launch(Dispatchers.Main) {
            val koreanDayOfWeek = when (date?.dayOfWeek?.value) {
                1 -> "(월)"
                2 -> "(화)"
                3 -> "(수)"
                4 -> "(목)"
                5 -> "(금)"
                6 -> "(토)"
                7 -> "(일)"
                else -> throw IllegalArgumentException()
            }
            val year = date.year
            val month = date.month.value
            val day = date.dayOfMonth
            datePickText.value = "${year}년 ${month}월 ${day}일 $koreanDayOfWeek"
        }
    }

    fun setTime() {
        val start = selectedStartTime.value
        var startString = ""
        val end = selectedEndTime.value
        var endString = ""

        startString = if (start!!.hour < 12 || start!!.hour == 24) {
            if (start!!.hour == 24) {
                String.format(Locale.getDefault(), "AM %02d:%02d", 12, start.minute)
            } else {
                String.format(Locale.getDefault(), "AM %02d:%02d", start.hour, start.minute)
            }
        } else {
            if (start!!.hour == 12) {
                String.format(Locale.getDefault(), "PM %02d:%02d", 12, start.minute)
            } else {
                String.format(Locale.getDefault(), "PM %02d:%02d", start.hour, start.minute)
            }
        }

        endString = if (end!!.hour < 12 || end!!.hour == 24) {
            if (end!!.hour == 24) {
                String.format(Locale.getDefault(), "AM %02d:%02d", 12, end.minute)
            } else {
                String.format(Locale.getDefault(), "AM %02d:%02d", end.hour, end.minute)
            }
        } else {
            if (end!!.hour == 12) {
                String.format(Locale.getDefault(), "PM %02d:%02d", 12, end.minute)
            } else {
                String.format(Locale.getDefault(), "PM %02d:%02d", end.hour, end.minute)
            }
        }
        timePickText.value = "$startString - $endString"
    }

    fun selectCrag(id: Long, name: String) {
        selectedCragEvent.value = Pair(id, name)
    }

    fun navigateToSelectCrag() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.NavigateToSelectCrag)
        }
    }

}