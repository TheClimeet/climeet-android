package com.climus.climeet.presentation.ui.intro.signup.climer.noticesetting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentNoticeSettingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeSettingFragment :
    BaseFragment<FragmentNoticeSettingBinding>(R.layout.fragment_notice_setting) {

    private val viewModel: NoticeSettingViewModel by viewModels()
    private val parentViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.climerSignUpProgress(6)
        setText()
        setBtnListener()
        initEventObserve()
    }

    private fun setText(){
        binding.tvNoticeSettingTitle.text = ClimerSignupForm.nickName + " 회원님!"
        binding.tvNoticeSettingNickname.text = ClimerSignupForm.nickName + "님,"
    }

    private fun setBtnListener(){
        binding.btnNoticeSettingAgreement.setOnClickListener {
            parentViewModel.checkAlarmPermission{
                ClimerSignupForm.setNotice(true)
                startSignUp()
            }
        }

        binding.tvNoticeSettingDisagree.setOnClickListener {
            ClimerSignupForm.setNotice(false)
            startSignUp()
        }

        binding.ivClimbingGoalBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun startSignUp(){
        viewModel.signUp(
            provider = ClimerSignupForm.socialType,
            accessToken = ClimerSignupForm.token,
            signUpRequest = ClimerSignupForm.toSignupRequest()
        )
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is NoticeSettingEvent.NavigateToBack -> findNavController().navigateUp()
                    is NoticeSettingEvent.NavigateToComplete -> navigateNext()
                    is NoticeSettingEvent.ShowToastMessage -> navigateNext() // 임시로 수정 showToastMessage(it.msg)
                }
            }
        }
    }

    private fun navigateNext() {
        val action = NoticeSettingFragmentDirections.actionNoticeSettingFragmentToCompletetFragment()
        findNavController().navigate(action)
    }
}