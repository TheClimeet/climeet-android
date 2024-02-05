package com.climus.climeet.presentation.ui.main.home.popularcrags.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager.CompleteDetailFragment
import com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager.LevelDetailFragment
import com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager.TimeDetailFragment

class PopularCragsVPAdpater (fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FollowerOrderFragment()
            else -> RecordOrderFragment()
        }
    }

}