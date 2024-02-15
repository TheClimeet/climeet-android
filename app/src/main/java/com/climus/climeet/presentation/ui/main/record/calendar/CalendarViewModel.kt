package com.climus.climeet.presentation.ui.main.record.calendar

import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.FollowCragEvent
import com.climus.climeet.presentation.ui.main.record.model.ClimbingRecordData
import com.climus.climeet.presentation.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarUiState(
    val recordList: List<ClimbingRecordData> = emptyList(),
    val emptyResultState: Boolean = false
)

sealed class CalendarEvent {
    data object NavigateToCreateClimbingRecord : CalendarEvent()

    data object NavigateToSelectDateBottomSheetFragment : CalendarEvent()

    data class ShowToastMessage(val msg: String): CalendarEvent()

    data object NavigateToTimerMain: CalendarEvent()
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: MainRepository,
    private val notificationManager: NotificationManagerCompat
) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CalendarEvent>()
    val event: SharedFlow<CalendarEvent> = _event.asSharedFlow()

    private var curJob: Job? = null

    val currentYear = MutableStateFlow("${YearMonth.now().year}")
    val currentMonth = MutableStateFlow("${YearMonth.now().month.value}")

    val isToday = MutableStateFlow(true)
    val isRecordVisible = MutableStateFlow(false)

    val selectedDate = MutableLiveData(LocalDate.now())

    init {
        setRecord(LocalDate.now())
    }

    fun updateSelectedYearMonth(selectedYear: String, selectedMonth: String) {
        currentYear.value = selectedYear
        currentMonth.value = selectedMonth
    }

    fun setIsToday(today: Boolean) {
        isToday.value = today
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    private suspend fun setGymProfile(gymId : Long): Pair<String, String> {
        return repository.getGymProfile(gymId).let { result ->
            when(result){
                is BaseState.Success -> {
                    val gymName = result.body.gymName
                    val gymProfile = result.body.gymProfileImageUrl
                    Pair(gymName, gymProfile)
                }
                is BaseState.Error -> {
                    _event.emit(CalendarEvent.ShowToastMessage(result.msg))
                    Pair("", "")
                }
            }
        }
    }

    fun setRecord(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                emptyResultState = true
            )
        }
        curJob = viewModelScope.launch {
            repository.getSelectDateRecord(date.toString(), date.toString()).let { result ->
                when(result){
                    is BaseState.Success -> {
                        if(result.body.isNotEmpty()){
                            val dataDeferred = result.body.map { item ->
                                async { setGymProfile(item.gymId) }
                            }
                            val data = dataDeferred.map { it.await() }
                            _uiState.update { state ->
                                state.copy(
                                    recordList = result.body.zip(data) { item, gymProfile ->
                                        item.toClimbingRecordData(gymProfile.first, gymProfile.second)
                                    },
                                    emptyResultState = false
                                )
                            }
                        } else {
                            _uiState.update { state ->
                                state.copy(
                                    recordList = emptyList(),
                                    emptyResultState = true
                                )
                            }
                        }
                    }
                    is BaseState.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                recordList = emptyList(),
                                emptyResultState = true
                            )
                        }
                        _event.emit(CalendarEvent.ShowToastMessage(result.msg))
                    }
                }
            }
        }
    }

    fun navigateToCreateClimbingRecord() {
        if(LocalDate.now().isBefore(selectedDate.value)){
            viewModelScope.launch{
                _event.emit(CalendarEvent.ShowToastMessage("과거의 기록을 적어주세요!"))
            }
        } else {
            viewModelScope.launch {
                _event.emit(CalendarEvent.NavigateToCreateClimbingRecord)
            }
        }
    }

    fun navigateToSelectDateBottomSheetFragment() {
        viewModelScope.launch {
            _event.emit(CalendarEvent.NavigateToSelectDateBottomSheetFragment)
        }
    }

    fun navigateToTimerMain(){
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

        if (areNotificationsEnabled) {
            viewModelScope.launch {
                _event.emit(CalendarEvent.NavigateToTimerMain)
            }
        } else {
            // 알림 권한이 허용되지 않았을 때 처리, 필요에 따라 수정
            viewModelScope.launch {
                _event.emit(CalendarEvent.ShowToastMessage("알림 권한을 허용해야 사용할 수 있습니다!"))
            }
        }
    }

}