package com.climus.climeet.presentation.ui.main.record.stats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed class StatsEvent {
    data object NavigateToSelectMonthYearBottomSheetFragment : StatsEvent()
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _event = MutableSharedFlow<StatsEvent>()
    val event: SharedFlow<StatsEvent> = _event.asSharedFlow()

    val selectedDate = MutableLiveData(LocalDate.now())
    val curDate =
        MutableStateFlow("${selectedDate.value?.year}년 ${selectedDate.value?.monthValue}월")

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
            selectedDate.value?.let {
                repository.getMyStatsMonth(it.year, it.monthValue).let { result ->
                    when(result){
                        is BaseState.Success -> {
                            Log.d("stattest", result.body.toString())
                        }
                        is BaseState.Error -> Log.d("stattest", result.msg)
                    }
                }
            }
        }
    }


}