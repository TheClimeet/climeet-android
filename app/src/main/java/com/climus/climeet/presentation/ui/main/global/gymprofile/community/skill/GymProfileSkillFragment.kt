package com.climus.climeet.presentation.ui.main.global.gymprofile.community.skill

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileSkillBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.stats.StatsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileSkillFragment:
    BaseFragment<FragmentGymProfileSkillBinding>(R.layout.fragment_gym_profile_skill) {

    private val statusViewModel: StatsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.statusVM = statusViewModel
    }

}