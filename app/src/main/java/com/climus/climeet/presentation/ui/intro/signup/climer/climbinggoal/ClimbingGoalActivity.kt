package com.climus.climeet.presentation.ui.intro.signup.climer.climbinggoal

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.climus.climeet.databinding.ActivityClimbingGoalBinding
import com.climus.climeet.presentation.base.BaseActivity
import com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting.NoticeSettingActivity

class ClimbingGoalActivity : BaseActivity<ActivityClimbingGoalBinding>(ActivityClimbingGoalBinding::inflate) {

    private lateinit var toggleImageViews: List<Pair<View, View>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toggleImageViews = listOf(
            binding.btnInstagramFacebookUnselected to binding.btnInstagramFacebookSelected,
            binding.btnYoutubeUnselected to binding.btnYoutubeSelected,
            binding.btnRecommendedUnselected to binding.btnRecommendedSelected,
            binding.btnBlogCafeUnselected to binding.btnBlogCafeSelected,
            binding.btnEtcUnselected to binding.btnEtcSelected
        )

        toggleImageViews.forEach { (unselectedIv, selectedIv) ->
            unselectedIv.setOnClickListener {
                toggleVisibility(selectedIv)
                toggleVisibility(unselectedIv)
            }
        }

        binding.btnNextValid.setOnClickListener {
            val intent = Intent(this, NoticeSettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toggleVisibility(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        } else {
            toggleImageViews.forEach { (unselectedIv, selectedIv) ->
                selectedIv.visibility = View.INVISIBLE
                unselectedIv.visibility = View.VISIBLE
            }
            view.visibility = View.VISIBLE
        }
    }
}