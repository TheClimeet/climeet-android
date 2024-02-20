package com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber

import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileBestClimberBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileBestClimberFragment : BaseFragment<FragmentGymProfileBestClimberBinding>(R.layout.fragment_gym_profile_best_climber) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBestClimer()
    }

    private fun setupBestClimer() {
        val bestClimerAdapter = GymProfileBestClimberAdapter(this)
        binding.vpBestClimerDetail.adapter = bestClimerAdapter

        val tabMenu = arrayListOf(" 완등 ", " 시간 ", " 레벨 ")
        TabLayoutMediator(binding.tbBestClimerDetail, binding.vpBestClimerDetail) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }
}