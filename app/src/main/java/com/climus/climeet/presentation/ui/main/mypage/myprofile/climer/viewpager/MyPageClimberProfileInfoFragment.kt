package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentEditClimberProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.stickchart.StickChartAdapter
import com.climus.climeet.presentation.ui.main.global.climerprofile.adapter.HomeGymAdapter
import com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager.ClimberProfileInfoViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.MyPageClimberProfileViewModel
import javax.inject.Inject

class MyPageClimberProfileInfoFragment @Inject constructor(
    private val userId: Long
) : BaseFragment<FragmentEditClimberProfileInfoBinding>(R.layout.fragment_edit_climber_profile_info) {

    private val sharedViewModel: ClimberProfileInfoViewModel by activityViewModels()
    private val mainViewModel: MyPageClimberProfileViewModel by activityViewModels()
    private val viewModel: MyPageClimberProfileInfoViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.rvHomeHomegym.adapter = HomeGymAdapter()
        binding.rvStickChart.adapter = StickChartAdapter()

        binding.svm = sharedViewModel
        binding.mainvm = mainViewModel
        binding.vm = viewModel

        sharedViewModel.setUserId(userId)

        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MyPageClimberProfileEvent.ChangePrivacyState -> setPrivacyState(it.target, it.state)
                }
            }
        }
    }

    // 공개 범위 설정
    private fun setPrivacyState(target: String, state: Boolean){
        mainViewModel.setPrivacyState(target, state)
    }
}