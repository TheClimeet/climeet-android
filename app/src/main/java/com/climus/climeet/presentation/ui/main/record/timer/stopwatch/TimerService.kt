package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.climus.climeet.R
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import android.app.NotificationChannelGroup
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.climus.climeet.presentation.ui.main.MainActivity


class TimerService : Service() {
    private var startTime = 0L
    private var pauseTime = 0L
    private var elapsedTime = 0L
    private var isPaused = MutableLiveData(false)
    private var isStopped = MutableLiveData(true)
    private var timer: Timer? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra("command")

        when (command) {
            "START" -> {
                startTimer()
                val notification = createNotification(0L)
                startForeground(1, notification)
            }

            "PAUSE" -> pauseTimer()
            "RESTART" -> restartTimer()
            "STOP" -> {
                stopTimer()
                stopForeground(true) // 서비스 백그라운드로 이동
                stopSelf() // 서비스 종료
                Log.d("timer", "서비스 종료")
            }
        }
        // 서비스 실행중
        serviceRunning.postValue(true)

        return START_STICKY
    }

    private fun createNotification(elapsedTime: Long): Notification {
        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTime)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60

        // 1시간 미만이면 분, 초만 보여준다
        val timeFormat = if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }

        // 알림창 클릭시 타이머 화면으로 이동
        val timerIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("showTimerFragment", true)
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, timerIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("$timeFormat")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notification.setSound(null)  // 알림음을 null로 설정
        }

        // 알림창 재시작, 일시중지 버튼
        if (isPaused.value == true) {
            val restartIntent = Intent(this, TimerService::class.java).apply {
                putExtra("command", "RESTART")
            }
            val restartPendingIntent: PendingIntent =
                PendingIntent.getService(this, 0, restartIntent, PendingIntent.FLAG_MUTABLE)
            // 재시작 액션 추가
            notification.addAction(R.drawable.ic_notification, "재시작", restartPendingIntent)
        } else {
            val pauseIntent = Intent(this, TimerService::class.java).apply {
                putExtra("command", "PAUSE")
            }
            val pausePendingIntent: PendingIntent =
                PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_MUTABLE)
            // 일시정지 액션 추가
            notification.addAction(R.drawable.ic_notification, "일시중지", pausePendingIntent)
        }

        return notification.build()
    }

    private fun createNotificationChannelAndGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Notification Channel Group 생성
            val groupName = "운동 설정" // 그룹 이름
            val channelGroup = NotificationChannelGroup(GROUP_ID, groupName)
            notificationManager.createNotificationChannelGroup(channelGroup)

            // Notification Channel 생성
            val channelName = "운동 시간"
            val descriptionText = "운동한 시간을 알려줍니다"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = descriptionText
                group = GROUP_ID // 채널을 그룹에 할당
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelAndGroup()
    }

    private fun setTimer(){
        if (isPaused.value == false) {
            startTime = System.currentTimeMillis()
        }
        timer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    elapsedTime += System.currentTimeMillis() - startTime
                    val notification = createNotification(elapsedTime)
                    val notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, notification) // 새로운 알림 생성

                    // 시간 브로드캐스트로 보내기
                    val intent = Intent("StopwatchUpdate")
                    intent.putExtra("elapsedTime", elapsedTime)
                    LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(intent)

                    startTime = System.currentTimeMillis()
                }
            }, 0, 1000)
        }
    }

    private fun startTimer() {
        isStopped.value = false
        setTimer()
        //Log.d("timer", "서비스 타이머 시작")
    }

    private fun pauseTimer() {
        isPaused.value = true
        timer?.cancel()
        pauseTime = elapsedTime
        // 알림창 재생성
        val notification = createNotification(elapsedTime)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        // TimeFragment에 전달
        val intent = Intent("StopwatchUpdate")
        intent.putExtra("pause", isPaused.value)
        intent.putExtra("pauseTime", pauseTime)
        LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(intent)

        Log.d("timer", "서비스 타이머 일시정지")
    }

    private fun restartTimer() {
        isPaused.value = false
        // 타이머 재시작
        setTimer()
        //Log.d("timer", "서비스 타이머 재시작")
    }

    private fun stopTimer() {
        isStopped.value = true
        timer?.cancel()

        // 스톱워치 화면 시간 0으로 바꿔주기
        val intent = Intent("StopwatchUpdate")
        intent.putExtra("elapsedTime", 0L)
        LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(intent)

        //Log.d("timer", "서비스 타이머 종료")
    }

    companion object {
        private const val CHANNEL_ID = "stopwatch_channel"
        private const val GROUP_ID = "exercise_record_group"
        val serviceRunning = MutableLiveData<Boolean>()
    }
}