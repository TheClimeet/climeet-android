package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.request.ClimbingRecord
import com.climus.climeet.data.model.request.CreateTimerClimbingRecordRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.data.local.ClimbingRecordData
import com.climus.climeet.data.local.RouteRecordData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: MainRepository
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

    var challengeNum = MutableLiveData<Long>()

    // 스톱워치 경과시간과 일시정지 여부를 전달받음
    // pauseState를 String으로 전달받는 이유 : default값을 설정하지 않기 위해
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _time.value = intent.getLongExtra("elapsedTime", 0L)
            val state = intent.getStringExtra("pauseState")
            if (state != null) {
                pauseState.value = state.toString()
                pauseTime.value = sharedPreferences.getLong("pauseTime", 0L)
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

    //
    fun sendClimbingRecord() = CoroutineScope(Dispatchers.Main).launch {
        val recordData: ClimbingRecordData
        val routeData: List<RouteRecordData>?
        val requestBody: CreateTimerClimbingRecordRequest

        recordData = withContext(Dispatchers.IO) { repository.getRecord(1) }
        routeData = withContext(Dispatchers.IO) { repository.getAllRoute() }

        // avgDifficulty 계산
        val avgDifficulty = if (routeData?.isNotEmpty() == true) {
            val totalDifficulty = routeData.sumBy { it.difficulty }
            totalDifficulty / routeData.size
        } else {
            0
        }

        // 루트 기록 생성
        val routeRecords = routeData.map { route ->
            ClimbingRecord(route.routeId, route.attemptCount, route.isCompleted)
        }

        var endTime = sharedPreferences.getString("stopTime", "00:00:00")

        if(endTime == "00:00"){
            endTime = "00:00:00"
        }
//        Log.d("recorddd", "시간 정보 : $endTime")
//        Log.d("recorddd", "평균 난이도 : $avgDifficulty\n기본 정보 : $recordData \n" +
//                "루트 기록 : $routeData")

        // 요청 바디 생성
        requestBody = CreateTimerClimbingRecordRequest(
            gymId = recordData.gymId,
            date = recordData.date,
            time = endTime!!,
            avgDifficulty = avgDifficulty,
            routeRecordRequestDtoList = routeRecords ?: listOf()
        )

//        viewModelScope.launch {
//            repository.createTimerClimbingRecord(requestBody).let {
//                when (it) {
//                    is BaseState.Success -> {
//                        // 성공
//                        Log.d("testss", it.toString())
//                    }
//
//                    is BaseState.Error -> {
//                        it.msg // 서버 에러 메시지
//                        Log.d("testss", it.msg)
//                    }
//                }
//            }
//        }

        // 다음 운동 기록을 위해 루트기록 초기화
        withContext(Dispatchers.IO) {
            repository.deleteAllRoute()
            repository.deleteAllRecord()
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
    }
}