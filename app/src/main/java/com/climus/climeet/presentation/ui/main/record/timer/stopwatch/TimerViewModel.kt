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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val _time = MutableLiveData<Long>().apply { value = 0L }
    val time: LiveData<Long> get() = _time

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _time.value = intent.getLongExtra("elapsedTime", 0)
        }
    }

    // 시간 설정
    val timeFormat: LiveData<String> = _time.map { elapsedTime ->
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

    fun registerReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(
            broadcastReceiver, IntentFilter(STOPWATCH_UPDATE)
        )
        Log.d("timer", "레지스터 연결")
    }

    fun unregisterReceiver(context: Context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)
        Log.d("timer", "레지스터 연결 해제")
    }

    companion object {
        const val STOPWATCH_UPDATE = "StopwatchUpdate"
    }
}