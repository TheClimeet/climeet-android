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

    val datePickText = MutableStateFlow("날짜를 선택해주세요")
    val selectedDate = MutableLiveData<LocalDate>()

    init {

    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
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

    fun navigateToSelectCrag() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.NavigateToSelectCrag)
        }
    }

}