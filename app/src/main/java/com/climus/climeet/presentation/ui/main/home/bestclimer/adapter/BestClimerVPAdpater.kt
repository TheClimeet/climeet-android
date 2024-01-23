package com.climus.climeet.presentation.ui.main.home.bestclimer.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.home.bestclimer.CompleteDetailFragment
import com.climus.climeet.presentation.ui.main.home.bestclimer.LevelDetailFragment
import com.climus.climeet.presentation.ui.main.home.bestclimer.TimeDetailFragment

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