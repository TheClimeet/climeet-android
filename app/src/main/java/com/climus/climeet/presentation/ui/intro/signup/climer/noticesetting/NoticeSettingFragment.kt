package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentNoticeSettingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm

class NoticeSettingFragment :
    BaseFragment<FragmentNoticeSettingBinding>(R.layout.fragment_notice_setting) {

    private val parentViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNoticeSettingTitle.text = ClimerSignupForm.nickName + " 회원님!"

        binding.btnNoticeSettingAgreement.setOnClickListener {
            parentViewModel.checkAlarmPermission{
                navigateNext()
            }
        }

        binding.tvNoticeSettingDisagree.setOnClickListener {
            ClimerSignupForm.setNotice(false)
            Log.d("Push Permission", ClimerSignupForm.noticePermission.toString())
            navigateNext()
        }

        binding.ivClimbingGoalBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvNoticeSettingNickname.text = ClimerSignupForm.nickName + "님,"
    }

    private fun navigateNext() {
        val action = NoticeSettingFragmentDirections.actionNoticeSettingFragmentToCompletetFragment()
        findNavController().navigate(action)
    }
}