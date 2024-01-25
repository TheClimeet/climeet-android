package com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BestClimerVPAdpater (fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CompleteDetailFragment()
            1 -> TimeDetailFragment()
            else -> LevelDetailFragment()
        }
    }
}