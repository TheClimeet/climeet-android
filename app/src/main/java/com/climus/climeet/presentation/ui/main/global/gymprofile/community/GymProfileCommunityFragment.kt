package com.climus.climeet.presentation.ui.main.global.gymprofile.community

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileCommunityBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.GymProfileBestClimberFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.completionrate.GymProfileAvgCompletionFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.skill.GymProfileSkillFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileCommunityFragment :
    BaseFragment<FragmentGymProfileCommunityBinding>(R.layout.fragment_gym_profile_community) {

    private val viewModel: GymProfileCommunityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initTabMenu()
    }

    private fun initTabMenu() {
        binding.tbCommuMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment: Fragment = when (tab.position) {
                    0 -> GymProfileSkillFragment()
                    1 -> GymProfileBestClimberFragment()
                    else -> GymProfileAvgCompletionFragment()
                }
                replaceFragment(fragment)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_menu, fragment)
        fragmentTransaction?.commit()
    }
}