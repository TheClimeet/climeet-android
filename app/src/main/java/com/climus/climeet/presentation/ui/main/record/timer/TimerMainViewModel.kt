package com.climus.climeet.presentation.ui.main.record.timer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//interface TimerStartedProvider {
//    fun isTimerStarted(): Boolean
//}

@HiltViewModel
class TimerMainViewModel @Inject constructor() : ViewModel() {

//    val stopwatchState: LiveData<StopwatchState> = false
//
//
//    override fun isTimerStarted(): Boolean {
//        Log.d("timer", "[메인뷰모델] 스톱워치 현상태 : ${stopwatchState.value?.isRunning}")
//        if((stopwatchState.value?.isRunning ?: false) == null){
//            return false
//        } else {
//            return stopwatchState.value?.isRunning ?: false
//        }
//    }
}