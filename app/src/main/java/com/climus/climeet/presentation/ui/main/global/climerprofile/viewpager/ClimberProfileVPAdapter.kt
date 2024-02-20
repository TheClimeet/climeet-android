package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ClimberProfileVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ClimberShortsFragment()
            else -> ClimberInfoFragment()
        }
    }

}