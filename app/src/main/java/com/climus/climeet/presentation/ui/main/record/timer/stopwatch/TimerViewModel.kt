package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.presentation.ui.main.record.timer.data.ClimbingDatabase
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesData
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: StopwatchStatesRepository
) : ViewModel(){

    private val _time = MutableLiveData<Long>().apply { value = 0L }
    private val time: LiveData<Long> get() = _time
    var pauseState = MutableLiveData<String>()

    var pauseTime = MutableLiveData<Long>()
    var isRunning = MutableLiveData<Boolean>().apply { value = false }
    var isStart = MutableLiveData<Boolean>().apply { value = false }
    var isPaused = MutableLiveData<Boolean>().apply { value = false }
    var isRestart = MutableLiveData<Boolean>().apply { value = false }
    var isStop = MutableLiveData<Boolean>().apply { value = true }

//    fun updateState(state: StopwatchStatesData) {
//        viewModelScope.launch {
//            Log.d("timer", "뷰모델 updataState 호출")
//            repository.updateState(state)
//        }
//    }
    init {
        if(TimerService.serviceRunning.value == null){
            Log.d("timer", "뷰모델 서비스 실행 안해서 초기화")
            isRunning.value = false
            isStart.value = false
            isPaused.value = false
            isRestart.value = false
            isStop.value = true
            pauseTime.value = 0L
        } else {
            isRunning.value = sharedPreferences.getBoolean(KEY_IS_RUNNING, isRunning.value!!)
            isStart.value = sharedPreferences.getBoolean(KEY_IS_START, isStart.value!!)
            isPaused.value = sharedPreferences.getBoolean(KEY_IS_PAUSE, isRunning.value!!)
            isRestart.value = sharedPreferences.getBoolean(KEY_IS_RESTART, isRestart.value!!)
            isStop.value = sharedPreferences.getBoolean(KEY_IS_STOP, isStop.value!!)
            pauseTime.value = sharedPreferences.getLong("pauseTime", 0L)
            Log.d(
                "timer",
                "뷰모델 init \nisStart : ${isStart.value}, isPause : ${isPaused.value}, isRestart : ${isRestart.value}, isStop : ${isStop.value}, isRunning : ${isStart.value}"
            )
        }
    }

    // 스톱워치 경과시간과 상태값 전달받음
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _time.value = intent.getLongExtra("elapsedTime", 0L)
            val state = intent.getStringExtra("pauseState")
            if(state != null){
                pauseState.value = state.toString()
                Log.d("timer", "알림창 일시정지 : ${pauseState.value}")
            }

//            Log.d("timer", "뷰모델 브로드캐스트 받는중")
//            pauseTime.value = intent.getLongExtra("pauseTime", 0L)
//            isRunning.value = intent.getBooleanExtra(KEY_IS_RUNNING, isRunning.value!!)
//            isStart.value = intent.getBooleanExtra(KEY_IS_START, isStart.value!!)
//            isPaused.value = intent.getBooleanExtra(KEY_IS_PAUSE, isPaused.value!!)
//            isRestart.value = intent.getBooleanExtra(KEY_IS_RESTART, isRestart.value!!)
//            isStop.value = intent.getBooleanExtra(KEY_IS_STOP, isStop.value!!)

//            viewModelScope.launch {
//                val state = repository.getState(0)
//                isStart.value = state.isStart
//                isStop.value = state.isStop
//                isPaused.value = state.isPaused
//                isRestart.value = state.isRestart
//                isRunning.value = state.isRunning
//            }
//            Log.d(
//                "timer",
//                "[브로드캐스트 받기] isStart : ${isStart.value}, isPause : ${isPaused.value}, isRestart : ${isRestart.value}, isStop : ${isStop.value}, isRunning : ${isRunning.value}"
//            )
        }
    }

    // 시간 설정
    val timeFormat: LiveData<String> = time.map { elapsedTime ->
        if (elapsedTime == 0L) {
            "00:00"
        } else {
            val hours = TimeUnit.MILLISECONDS.toHours(elapsedTime)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60

            if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }

    val pauseTimeFormat: LiveData<String> = pauseTime.map { pauseTime ->
        if (pauseTime == 0L) {
            "00:00"
        } else {
            val hours = TimeUnit.MILLISECONDS.toHours(pauseTime)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(pauseTime) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(pauseTime) % 60

            if (hours > 0) {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }

    fun registerReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            broadcastReceiver, IntentFilter(STOPWATCH_UPDATE)
        )
    }

    fun unregisterReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)
    }

    companion object {
        const val STOPWATCH_UPDATE = "StopwatchUpdate"
        const val KEY_IS_START = "isStart"
        const val KEY_IS_PAUSE = "isPause"
        const val KEY_IS_RESTART = "isRestart"
        const val KEY_IS_STOP = "isStop"
        const val KEY_IS_RUNNING = "isRunning"
    }
}