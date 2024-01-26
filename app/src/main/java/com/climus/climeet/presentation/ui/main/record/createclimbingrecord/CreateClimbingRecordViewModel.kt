package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import android.util.Log
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
}

@HiltViewModel
class CreateClimbingRecordViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableSharedFlow<CreateClimbingRecordEvent>()
    val event: SharedFlow<CreateClimbingRecordEvent> = _event.asSharedFlow()

    val datePickText = MutableStateFlow("날짜를 선택해주세요")
    init {

    }

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.ShowDatePicker)
        }
    }

    fun setDate(){
        viewModelScope.launch(Dispatchers.Main) {
            Log.d("dateTest", "vm ${CreateRecordData.selectedDate}")
            val date = CreateRecordData.selectedDate
            val koreanDayOfWeek = when (date.dayOfWeek.value) {
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
            Log.d("dateTest", "dhks ${datePickText.value}")
        }
    }

}