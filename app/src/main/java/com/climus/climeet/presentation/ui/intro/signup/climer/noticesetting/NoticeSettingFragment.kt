package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentNoticeSettingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.climbinggoal.ClimbingGoalFragmentDirections
import com.climus.climeet.presentation.ui.intro.signup.climer.complete.CompleteActivity

class NoticeSettingFragment : BaseFragment<FragmentNoticeSettingBinding>(R.layout.fragment_notice_setting) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNoticeSettingAgreement.setOnClickListener {
            findNavController().toComplete()
        }

        binding.tvNoticeSettingDisagree.setOnClickListener {
            findNavController().toComplete()
        }

        binding.ivClimbingGoalBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun NavController.toComplete() {
        val action = NoticeSettingFragmentDirections.actionNoticeSettingFragmentToCompletetFragment()
        navigate(action)
    }
}