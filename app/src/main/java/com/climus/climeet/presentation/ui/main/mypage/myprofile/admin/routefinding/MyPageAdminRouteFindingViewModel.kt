package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.LevelColor
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.LevelColorData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.RouteColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

data class MyPageAdminRouteFindingUiState(
    val selectedLevelColor: List<LevelColor> = listOf(
        LevelColor(RouteColor("하양", "#FFFFFF"), "V1")
    )
)

sealed class MyPageAdminRouteFindingEvent {
    data object ShowDatePicker : MyPageAdminRouteFindingEvent()
    data object ShowSetLevel : MyPageAdminRouteFindingEvent()
}

@HiltViewModel
class MyPageAdminRouteFindingViewModel @Inject constructor(

) : ViewModel() {
    private val _event = MutableSharedFlow<MyPageAdminRouteFindingEvent>()
    val event: SharedFlow<MyPageAdminRouteFindingEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(MyPageAdminRouteFindingUiState())
    val uiState: StateFlow<MyPageAdminRouteFindingUiState> = _uiState.asStateFlow()

    private val initDate = LocalDate.now()
    val selectedDateText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일 (${dayOfWeekMap[initDate.dayOfWeek]})")
    val selectedDate = MutableStateFlow(initDate)

    val selectedColor = MutableStateFlow(RouteColor("-", "#FFFFFF"))
    val colorList = LevelColorData.COLORS

    val selectedLevel = MutableStateFlow("레벨 설정")

    fun setSelectedDate(updateDate: LocalDate) {
        selectedDate.update { updateDate }
        selectedDateText.update {
            "${updateDate.year}년 ${updateDate.monthValue}월 ${updateDate.dayOfMonth}일 (${dayOfWeekMap[updateDate.dayOfWeek]})"
        }
    }

    fun selectColor(color: RouteColor) {
        selectedColor.value = color
    }

    fun selectLevel(level: String) {
        selectedLevel.value = level
    }

    fun noUse(dateDate: LocalDate) {

    }

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.ShowDatePicker)
        }
    }

    fun showSetLevel() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.ShowSetLevel)
        }
    }

    companion object {
        private val dayOfWeekMap = mapOf(
            DayOfWeek.MONDAY to "월",
            DayOfWeek.TUESDAY to "화",
            DayOfWeek.WEDNESDAY to "수",
            DayOfWeek.THURSDAY to "목",
            DayOfWeek.FRIDAY to "금",
            DayOfWeek.SATURDAY to "토",
            DayOfWeek.SUNDAY to "일"
        )
    }

}