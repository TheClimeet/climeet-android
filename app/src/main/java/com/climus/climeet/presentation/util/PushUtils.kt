package com.climus.climeet.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager

object PushUtils {
    private var mWakeLock: PowerManager.WakeLock? = null

    @SuppressLint("InvalidWakeLockTag")
    fun acquireWakeLock(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "WAKEUP"
        )
        mWakeLock!!.acquire(3000)
    }

    fun releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock!!.release()
            mWakeLock = null
        }
    }
}