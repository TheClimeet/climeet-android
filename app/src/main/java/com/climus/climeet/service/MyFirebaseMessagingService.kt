package com.climus.climeet.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.presentation.ui.splash.SplashActivity
import com.climus.climeet.presentation.util.PushUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        PushUtils.acquireWakeLock(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        PushUtils.acquireWakeLock(App.getContext())
        //수신한 메시지를 처리

        showNotification(message.data["title"], message.data["body"])
    }

    private fun showNotification(title: String? = "", message: String? = "") {

        val uniId = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, SplashActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this,
            uniId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "greengrim"

        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            priority = NotificationManager.IMPORTANCE_HIGH
            setContentTitle(title)
            setContentText(message)
            setContentIntent(pIntent)
            setAutoCancel(true)
            color = Color.argb(1, 120, 63, 59)
            setColorized(true)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            setSmallIcon(R.mipmap.ic_launcher)
        }

        // Head up 알람 설정
        notificationBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setFullScreenIntent(pIntent, true)

        getSystemService(NotificationManager::class.java).run {
            val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
            createNotificationChannel(channel)

            notify(uniId, notificationBuilder.build())
        }
    }


    suspend fun getFirebaseToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                continuation.resume(token)
            } else {
                continuation.resumeWithException(
                    task.exception ?: RuntimeException("Error getting FCM token")
                )
            }
        }
    }

}