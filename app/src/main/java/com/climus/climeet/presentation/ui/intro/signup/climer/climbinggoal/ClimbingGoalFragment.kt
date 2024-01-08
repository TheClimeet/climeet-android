package com.climus.climeet.presentation.ui.intro.signup.climer.climbinggoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimbingGoalBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.FollowCragFragmentDirections

class ClimbingGoalFragment : BaseFragment<FragmentClimbingGoalBinding>(R.layout.fragment_climbing_goal) {

    private lateinit var toggleImageViews: List<Pair<View, View>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                binding.btnNextValid.visibility = View.VISIBLE
                binding.btnNextArrowValid.visibility = View.VISIBLE
                binding.btnNextInvalid.visibility = View.INVISIBLE
                binding.btnNextArrowInvalid.visibility = View.INVISIBLE
            }
        }

        binding.ivClimbingGoalBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNextValid.setOnClickListener {
            findNavController().toNoticeSetting()
        }

        binding.tvClimbingGoalNickname.text = ClimerSignupForm.nickName + "님,"
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

    private fun NavController.toNoticeSetting() {
        val action = ClimbingGoalFragmentDirections.actionClimbingGoalFragmentToNoticeSettingFragment()
        navigate(action)
    }
}