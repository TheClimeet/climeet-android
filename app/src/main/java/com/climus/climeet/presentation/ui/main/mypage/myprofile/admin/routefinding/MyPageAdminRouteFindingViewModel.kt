package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord.CreateClimbingRecordEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class MyPageAdminRouteFindingEvent {
    data object ShowDatePicker : MyPageAdminRouteFindingEvent()
}

@HiltViewModel
class MyPageAdminRouteFindingViewModel @Inject constructor(

) : ViewModel() {
    private val _event = MutableSharedFlow<MyPageAdminRouteFindingEvent>()
    val event: SharedFlow<MyPageAdminRouteFindingEvent> = _event.asSharedFlow()

    val initDate = LocalDate.now()
    val selectedDateText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일 (${initDate.dayOfWeek})")
    val selectedDate = MutableStateFlow(initDate)

    fun setSelectedDate(updateDate: LocalDate) {
        selectedDate.update { updateDate }
        selectedDateText.update { "${updateDate}년 ${updateDate}월 ${updateDate}일 (${updateDate})" }
    }

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.ShowDatePicker)
        }
    }

    fun noUse(dateDate : LocalDate) {

    }

}