package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimbingRecord
import com.climus.climeet.data.model.request.CreateTimerClimbingRecordRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordData
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordRepository
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordData
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: MainRepository,
    private val routeRepository: RouteRecordRepository,
    private val climbingRepository: ClimbingRecordRepository
) : ViewModel() {

    private val _time = MutableLiveData<Long>().apply { value = 0L }
    private val time: LiveData<Long> get() = _time
    var pauseState = MutableLiveData<String>()

    var pauseTime = MutableLiveData<Long>()
    var isRunning = MutableLiveData<Boolean>().apply { value = false }
    var isStart = MutableLiveData<Boolean>().apply { value = false }
    var isPaused = MutableLiveData<Boolean>().apply { value = false }
    var isRestart = MutableLiveData<Boolean>().apply { value = false }
    var isStop = MutableLiveData<Boolean>().apply { value = true }

    // 스톱워치 경과시간과 일시정지 여부를 전달받음
    // pauseState를 String으로 전달받는 이유 : default값을 설정하지 않기 위해
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _time.value = intent.getLongExtra("elapsedTime", 0L)
            val state = intent.getStringExtra("pauseState")
            if (state != null) {
                pauseState.value = state.toString()
                pauseTime.value = sharedPreferences.getLong("pauseTime", 0L)
                Log.d("TIMER", "알림창 일시정지 : ${pauseState.value}")
            }
        }
    }

    // 흘러가는 시간 형식 설정
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

    // 일시정지 시간 형식 설정
    // 서비스에서 보낸 broadcast를 통해 pauseTime값이 업데이트 되면, pauseTimeFormat을 설정한다
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

    // todo : roomDB값으로 루트기록 넘겨주기
    fun sendClimbingRecord() = CoroutineScope(Dispatchers.Main).launch {
//        val recordData: ClimbingRecordData
//        val routeData: List<RouteRecordData>?
        val requestBody: CreateTimerClimbingRecordRequest
//
//        // todo : room db에서 정보 가져오기
//        // gymId, date, time, avgDifficulty 가져오기
//        recordData = withContext(Dispatchers.IO) { climbingRepository.getRoute(1) }
//
//        // routeRecordRequestDtoList : routeId, attemptCount, isCompleted 가져오기
//        routeData = withContext(Dispatchers.IO) { routeRepository.getAllRecord() }
//
//        delay(1000)  // 1초 대기
//        Log.d("room", "기본 정보 : $recordData.toString()\n 루트 기록 : $routeData.toString()")
//
//        // 루트 기록 생성
//        val routeRecords = routeData.map { route ->
//            ClimbingRecord(route.routeId, route.attemptCount, route.isCompleted)
//        }
        // 요청 바디 생성
//        requestBody = CreateTimerClimbingRecordRequest(
//            gymId = recordData.gymId,
//            date = recordData.date,
//            time = LocalTime.parse(recordData.time), // String을 LocalTime으로 변환
//            avgDifficulty = recordData.avgDifficulty,
//            routeRecordRequestDtoList = routeRecords
//        )

        // todo : 더미값으로 테스트중
        val time = "00:05:15"
        val routeRecords = listOf(
            ClimbingRecord(routeId = 1, attemptCount = 5, isCompleted = true)
        )

        requestBody = CreateTimerClimbingRecordRequest(
            gymId = 1,
            date = "2024-02-11",
            time = time, // String을 LocalTime으로 변환
            avgDifficulty = 5,
            routeRecordRequestDtoList = routeRecords
        )

        viewModelScope.launch {
            repository.createTimerClimbingRecord(requestBody).let {
                when (it) {
                    is BaseState.Success -> {
                        // 성공
                        Log.d("testss", it.toString())
                    }

                    is BaseState.Error -> {
                        it.msg // 서버 에러 메시지
                        Log.d("testss", it.msg)
                    }
                }
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