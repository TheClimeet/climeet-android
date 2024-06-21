package com.climus.climeet.presentation.ui.main.mypage.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAlarmBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageAlarmFragment: BaseFragment<FragmentMypageAlarmBinding>(R.layout.fragment_mypage_alarm) {

    private val viewModel: MyPageAlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initSwitchCheckedListener()
    }

    private fun initSwitchCheckedListener(){
        // 팔로워
        binding.swFollower.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (compoundButton?.isPressed == true)
                viewModel.setFollowerSwitchState(isChecked)
        }
        // 좋아요
        binding.swLike.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (compoundButton?.isPressed == true)
                viewModel.setLikeSwitchState(isChecked)
        }
        // 댓글
        binding.swComment.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (compoundButton?.isPressed == true)
                viewModel.setCommentSwitchState(isChecked)
        }
        // 앱 푸시
        binding.swAppPush.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (compoundButton?.isPressed == true)
                viewModel.setAppPushSwitchState(isChecked)
        }
    }
}