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
import com.climus.climeet.presentation.ui.main.MainActivity


class TimerService : Service() {
    private var startTime = 0L
    private var elapsedTime = 0L
    private var isPaused = false
    private var timer: Timer? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra("command")

        when (command) {
            "START" -> {
                startTimer()
                val notification = createNotification(0)
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

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("showTimerFragment", true)
        }
        // 알림창 클릭시 타이머 화면으로 이동
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("$timeFormat")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(null)  // 알림음을 null로 설정
        }

        return builder.build()
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

    private fun startTimer() {
        if (!isPaused) {
            startTime = System.currentTimeMillis()
        }
        timer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    elapsedTime += System.currentTimeMillis() - startTime
                    val notification = createNotification(elapsedTime)
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, notification)
                    startTime = System.currentTimeMillis()
                }
            }, 0, 1000)
        }
        Log.d("timer", "서비스 타이머 시작")
    }

    private fun pauseTimer() {
        isPaused = true
        timer?.cancel()
        Log.d("timer", "서비스 타이머 일시정지")
    }

    private fun restartTimer() {
        if (isPaused) {
            isPaused = false
            startTimer()
            Log.d("timer", "서비스 타이머 재시작")
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        Log.d("timer", "서비스 타이머 종료")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("timer", "서비스 삭제")
    }

    companion object {
        private const val CHANNEL_ID = "stopwatch_channel"
        private const val GROUP_ID = "exercise_record_group"
    }
}