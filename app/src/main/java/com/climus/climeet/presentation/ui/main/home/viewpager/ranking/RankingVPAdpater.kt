package com.climus.climeet.presentation.ui.main.home.viewpager.best

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.TimeFragment

class RankingVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CompleteClimbingFragment()
            1 -> TimeFragment()
            else -> LevelFragment()
        }
    }
}