package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowerFragment
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowingFragment

class MyShortsVPAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {

            0 -> LikeFragment()
            1-> CommentFragment()
            else -> SaveFragment()

        }
    }
}