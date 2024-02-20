package com.climus.climeet.presentation.ui.main.global.gymprofile.community.completionrate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileAvgCompletionBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.stickchart.StickChartAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileAvgCompletionFragment :
    BaseFragment<FragmentGymProfileAvgCompletionBinding>(R.layout.fragment_gym_profile_avg_completion) {

    private val viewModel: GymProfileAvgCompletionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvStickChart.adapter = StickChartAdapter()
    }

}