package com.climus.climeet.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
    }


}