package com.climus.climeet.presentation.ui.main.record.bottomsheet.selecttime

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale
import javax.inject.Inject

sealed class SelectTimeBottomEvent {
    data object CloseFragment : SelectTimeBottomEvent()

    data object SetTime : SelectTimeBottomEvent()

    data object ClickStart : SelectTimeBottomEvent()

    data object ClickEnd : SelectTimeBottomEvent()
}

@HiltViewModel
class SelectTimeBottomViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<SelectTimeBottomEvent>()
    val event: SharedFlow<SelectTimeBottomEvent> = _event.asSharedFlow()

    val selectState = MutableStateFlow(true)
    val startTime = MutableStateFlow(CreateRecordData.selectedStartTime)
    val endTime = MutableStateFlow(CreateRecordData.selectedEndTime)
    val startTimeString = MutableLiveData("")
    val endTimeString = MutableLiveData("")

    init {
        setStartTime(startTime.value.hour, startTime.value.minute)
        setEndTime(endTime.value.hour, endTime.value.minute)
    }

    fun closeFragment() {
        viewModelScope.launch {
            _event.emit(SelectTimeBottomEvent.CloseFragment)
        }
    }

    fun setTime() {
        CreateRecordData.setSelectedTime(startTime.value, endTime.value)
        viewModelScope.launch {
            _event.emit(SelectTimeBottomEvent.SetTime)
        }
    }

    fun clickStart() {
        selectState.value = true
        viewModelScope.launch {
            _event.emit(SelectTimeBottomEvent.ClickStart)
        }
    }

    fun clickEnd() {
        selectState.value = false
        viewModelScope.launch {
            _event.emit(SelectTimeBottomEvent.ClickEnd)
        }
    }

    fun setStartTime(hour: Int, min: Int) {
        startTime.value = LocalTime.of(hour, min, 0)
        if (hour < 12 || hour == 24) {
            if (hour == 24) {
                startTimeString.value =
                    String.format(Locale.getDefault(), "오전 %02d:%02d", 12, min)
            } else {
                startTimeString.value =
                    String.format(Locale.getDefault(), "오전 %02d:%02d", hour, min)
            }

        } else {
            if (hour == 12) {
                startTimeString.value =
                    String.format(Locale.getDefault(), "오후 %02d:%02d", 12, min)
            } else {
                startTimeString.value =
                    String.format(Locale.getDefault(), "오후 %02d:%02d", hour - 12, min)
            }
        }
    }

    fun setEndTime(hour: Int, min: Int) {
        endTime.value = LocalTime.of(hour, min, 0)
        if (hour < 12 || hour == 24) {
            if (hour == 24) {
                endTimeString.value =
                    String.format(Locale.getDefault(), "오전 %02d:%02d", 12, min)
            } else {
                endTimeString.value =
                    String.format(Locale.getDefault(), "오전 %02d:%02d", hour, min)
            }

        } else {
            if (hour == 12) {
                endTimeString.value =
                    String.format(Locale.getDefault(), "오후 %02d:%02d", 12, min)
            } else {
                endTimeString.value =
                    String.format(Locale.getDefault(), "오후 %02d:%02d", hour - 12, min)
            }
        }
    }
}