package com.climus.climeet.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.climus.climeet.BuildConfig
import com.climus.climeet.presentation.util.Constants.TAG
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    init {
        instance = this
    }

    companion object{
        lateinit var instance : App
        lateinit var sharedPreferences: SharedPreferences
        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences =
            applicationContext.getSharedPreferences("APP", MODE_PRIVATE)
        initSocialLogin()
    }

    private fun initSocialLogin(){
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
//        NaverIdLoginSDK.initialize(this,
//            BuildConfig.NAVER_CLIENT_ID,
//            BuildConfig.NAVER_CLIENT_SECRET,
//            BuildConfig.NAVER_CLIENT_NAME
//        )
    }


}