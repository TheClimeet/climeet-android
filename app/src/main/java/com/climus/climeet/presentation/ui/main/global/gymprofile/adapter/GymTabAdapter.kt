package com.climus.climeet.presentation.ui.main.global.gymprofile.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.GymProfileCommunityFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.info.GymProfileInfoFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.route.GymProfileRouteFragment

class GymTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GymProfileCommunityFragment()
            1 -> GymProfileRouteFragment()
            else -> GymProfileInfoFragment()
        }
    }
}