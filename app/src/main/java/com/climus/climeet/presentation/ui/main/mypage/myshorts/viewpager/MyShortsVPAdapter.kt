package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyShortsVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPageMyShortsLikeFragment()
            1 -> MyPageMyShortsCommentFragment()
            else -> MyPageMyShortsSaveFragment()
        }
    }
}