package com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.detail.complete.GymProfileCompleteDetailFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.detail.level.GymProfileLevelDetailFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.detail.time.GymProfileTimeDetailFragment

class GymProfileBestClimberAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> GymProfileCompleteDetailFragment()
                1 -> GymProfileTimeDetailFragment()
                else -> GymProfileLevelDetailFragment()
            }
        }
    }