package com.climus.climeet.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.climus.climeet.BuildConfig
import com.climus.climeet.presentation.util.Constants.TAG
import com.climus.climeet.service.MyFirebaseMessagingService
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application(){

    init {
        instance = this
    }

    companion object{
        lateinit var instance : App
        lateinit var sharedPreferences: SharedPreferences
        var fcmToken = ""
        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences =
            applicationContext.getSharedPreferences("APP", MODE_PRIVATE)
        initSocialLogin()
        getFCMToken()
    }

    private fun initSocialLogin(){
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_CLIENT_NAME
        )
    }

    private fun getFCMToken() {
        FirebaseApp.initializeApp(this@App)
        CoroutineScope(Dispatchers.Main).launch {
            fcmToken = async { MyFirebaseMessagingService().getFirebaseToken() }.await()
            Log.d("fcmToken", fcmToken)
        }
    }


}