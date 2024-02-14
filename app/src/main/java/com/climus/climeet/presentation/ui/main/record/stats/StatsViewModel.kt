package com.climus.climeet.presentation.ui.main.record.stats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class StatusUiState(
    val totalTime: String = "00:00:00",
    val totalCompletedCount: Int = 0,
    val totalAttemptCount: Int = 0
)

sealed class StatsEvent {
    data object NavigateToSelectMonthYearBottomSheetFragment : StatsEvent()
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatusUiState())
    val uiState: StateFlow<StatusUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<StatsEvent>()
    val event: SharedFlow<StatsEvent> = _event.asSharedFlow()

    val selectedDate = MutableLiveData(LocalDate.now())
    val curDate =
        MutableStateFlow("${selectedDate.value?.year}년 ${selectedDate.value?.monthValue}월")

    init {
        getMyStatus()
    }

    fun navigateToSelectMonthYearBottomSheetFragment() {
        viewModelScope.launch {
            _event.emit(StatsEvent.NavigateToSelectMonthYearBottomSheetFragment)
        }
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
        curDate.value = "${date.year}년 ${date.monthValue}월"
        getMyStatus()
    }

    fun getMyStatus() {
        viewModelScope.launch {
            val date = selectedDate.value?.let {
                it
            } ?: run {
                LocalDate.of(0, 0, 0)
            }
            repository.getMyStatsMonth(date.year, date.monthValue).let { result ->
                when (result) {
                    is BaseState.Success -> {
                        val body = result.body
                        _uiState.update { state ->
                            state.copy(
                                totalTime = body.time,
                                totalCompletedCount = body.totalCompletedCount,
                                totalAttemptCount = body.attemptRouteCount
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                totalTime = "00:00:00",
                                totalCompletedCount = 0,
                                totalAttemptCount = 0
                            )
                        }
                    }
                }
            }
        }
    }
}