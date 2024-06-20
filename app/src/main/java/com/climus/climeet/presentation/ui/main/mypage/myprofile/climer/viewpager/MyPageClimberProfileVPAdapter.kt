package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageClimberProfileVPAdapter(
    fragment: Fragment,
    private val userId: Long
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPageClimberProfileShortsFragment(userId)
            else -> MyPageClimberProfileInfoFragment(userId)
        }
    }

}