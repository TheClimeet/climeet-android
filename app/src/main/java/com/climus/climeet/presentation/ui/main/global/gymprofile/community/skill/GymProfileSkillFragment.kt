package com.climus.climeet.presentation.ui.main.global.gymprofile.community.skill

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileSkillBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.stickchart.StickChartAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileSkillFragment:
    BaseFragment<FragmentGymProfileSkillBinding>(R.layout.fragment_gym_profile_skill) {

    private val viewModel: GymProfileSkillViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvStickChart.adapter = StickChartAdapter()
    }

}