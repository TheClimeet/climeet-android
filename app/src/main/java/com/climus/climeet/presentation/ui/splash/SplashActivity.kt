package com.climus.climeet.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.climus.climeet.databinding.ActivitySplashBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.ui.intro.IntroActivity
import com.climus.climeet.presentation.ui.main.MainActivity
import com.climus.climeet.presentation.ui.onboard.OnBoardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEventObserve()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkLoginType()
        }, 2000)
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SplashEvent.NavigateToIntroActivity -> {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                IntroActivity::class.java
                            )
                        )
                        delay(500)
                        finish()
                    }

                    is SplashEvent.NavigateToMainActivity -> {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                MainActivity::class.java
                            )
                        )
                        delay(500)
                        finish()
                    }

                    is SplashEvent.NavigateToOnboardActivity -> {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                OnBoardActivity::class.java
                            )
                        )
                        delay(500)
                        finish()
                    }
                }
            }
        }
    }
}