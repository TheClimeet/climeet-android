package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimberProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.stickchart.StickChartAdapter
import com.climus.climeet.presentation.ui.main.global.climerprofile.adapter.HomeGymAdapter
import com.climus.climeet.presentation.ui.toGymProfile
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClimberProfileInfoFragment @Inject constructor(private val userId: Long) :
    BaseFragment<FragmentClimberProfileInfoBinding>(R.layout.fragment_climber_profile_info) {


    private val viewModel: ClimberProfileInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.rvHomeHomegym.adapter = HomeGymAdapter()
        binding.rvStickChart.adapter = StickChartAdapter()
        binding.vm = viewModel
        viewModel.setUserId(userId)

        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ClimberProfileEvent.NavigateToGymProfile -> findNavController().toGymProfile(
                        it.id
                    )
                }
            }
        }
    }
}