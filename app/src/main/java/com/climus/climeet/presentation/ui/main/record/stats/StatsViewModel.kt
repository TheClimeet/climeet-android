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
import com.climus.climeet.presentation.util.Constants.TAG
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
                        _uiState.update { state ->
                            state.copy(
                                totalTime = body.time,
                                totalCompletedCount = body.totalCompletedCount,
                                totalAttemptCount = body.attemptRouteCount
                            )
                        }

                        // todo 여기부터가 차트그릴 데이터 파싱하는 부분
                        val list = mutableListOf<StickChartUiData>()

                        // todo 받은 차트값중에서 max값을 고르는 for문
                        var maxPercent = -1f
                        body.difficulty.forEach {
                            if (maxPercent < it.value.toFloat()) {
                                maxPercent = it.value.toFloat()
                            }
                        }

                        // todo max값 차트값을 기준으로 퍼센테이지를 계산하는 로직

                        body.difficulty.forEach {
                            val percent = if (it.value == 0) {
                                0
                            } else {
                                ((it.value.toFloat() / body.totalCompletedCount.toFloat()) * 100).roundToInt()
                            }

                            list.add(
                                StickChartUiData(
                                    // todo 차트 꼭대기 퍼센트 스트링
                                    percentString = "$percent%",
                                    // todo 차트 막대 길이비율 정하는 float값
                                    percent = if(percent == 0) 0f else (it.value.toFloat() / maxPercent) * 0.8f,
                                    // todo 차트 하단에 레벨이름
                                    levelName = it.key,
                                    // todo 레벨에 대응되는 색상 hex 값
                                    levelHex = Constants.climeetColor[it.key]
                                )
                            )
                        }

                        _uiState.update { state ->
                            state.copy(
                                chartUiList = list
                            )
                        }

                        // todo 여기까지

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

    fun setProgress() {
        cc.value = "${uiState.value.totalCompletedCount}문제 완등"
        ac.value = "${uiState.value.totalAttemptCount}문제 도전!"
    }

    fun btnMoveDate(isPlus: Boolean) {
        var date = selectedDate.value?.let {
            it
        } ?: run {
            LocalDate.of(0, 0, 0)
        }
        if (isPlus) {
            selectedDate.value = date.plusMonths(1)
        } else {
            selectedDate.value = date.minusMonths(1)
        }
        date = selectedDate.value
        curDate.value = "${date.year}년 ${date.monthValue}월"
        getMyStatus()
    }

}