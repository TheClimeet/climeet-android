package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.climus.climeet.R
import com.climus.climeet.databinding.ActivityClimbingGoalBinding
import com.climus.climeet.presentation.base.BaseActivity

class CompleteActivity : BaseActivity<ActivityClimbingGoalBinding>(ActivityClimbingGoalBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)
    }
}