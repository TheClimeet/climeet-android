package com.climus.climeet.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.climus.climeet.app.App
import com.climus.climeet.databinding.ActivitySplashBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.ui.intro.IntroActivity
import com.climus.climeet.presentation.ui.main.MainActivity
import com.climus.climeet.presentation.util.Constants.X_ACCESS_TOKEN

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Handler(Looper.getMainLooper()).postDelayed({

            val jwt: String? = App.sharedPreferences.getString(X_ACCESS_TOKEN, null)
            jwt?.let {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } ?: run {
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
        }, 2000)
    }
}