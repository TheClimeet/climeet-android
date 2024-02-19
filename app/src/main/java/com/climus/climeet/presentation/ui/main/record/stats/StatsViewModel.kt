package com.climus.climeet.presentation.ui.main.record.stats

import android.util.Log
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.stickchart.StickChartUiData
import com.climus.climeet.presentation.util.Constants
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
import kotlin.math.roundToInt

data class StatusUiState(
    val totalTime: String = "00:00:00",
    val totalCompletedCount: Int = 0,
    val totalAttemptCount: Int = 0,
    val completedCountString: String = "0문제 완등",
    val attemptCountString: String = "100문제 도전!",
    val chartUiList: List<StickChartUiData> = emptyList()
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
    val cc = MutableStateFlow("0문제 완등")
    val ac = MutableStateFlow("100문제 도전!")

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
                        Log.d("stattest", body.toString())
                        _uiState.update { state ->
                            state.copy(
                                totalTime = body.time,
                                totalCompletedCount = body.totalCompletedCount,
                                totalAttemptCount = body.attemptRouteCount
                            )
                        }

                        val list = mutableListOf<StickChartUiData>()

                        var maxPercent = -1f
                        body.difficulty.forEach{
                            if( maxPercent < it.value.toFloat()){
                                maxPercent = it.value.toFloat()
                            }
                        }

                        body.difficulty.forEach {
                            val percent = ((it.value.toFloat() / body.totalCompletedCount.toFloat()) * 100).roundToInt()
                            list.add(
                                StickChartUiData(
                                    percentString = "$percent%",
                                    percent = (it.value.toFloat() / maxPercent) * 0.8f,
                                    levelName = it.key,
                                    levelHex = Constants.climeetColor[it.key]
                                )
                            )
                        }

                        _uiState.update { state ->
                            state.copy(
                                chartUiList = list
                            )
                        }

                        setProgress()
                    }

                    is BaseState.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                totalTime = "00:00:00",
                                totalCompletedCount = 0,
                                totalAttemptCount = 0
                            )
                        }
                        setProgress()
                    }
                }
            }
        }
    }

    fun setProgress(){
        cc.value = "${uiState.value.totalCompletedCount}문제 완등"
        ac.value = "${uiState.value.totalAttemptCount}문제 도전!"
    }

    fun btnMoveDate(isPlus: Boolean){
        var date = selectedDate.value?.let {
            it
        } ?: run {
            LocalDate.of(0, 0, 0)
        }
        if(isPlus){
            selectedDate.value = date.plusMonths(1)
        }else{
            selectedDate.value = date.minusMonths(1)
        }
        date = selectedDate.value
        curDate.value = "${date.year}년 ${date.monthValue}월"
        getMyStatus()
    }

}