package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.community.MyPageAdminProfileCommunityFragment
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.info.MyPageAdminProfileInfoFragment
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.route.MyPageAdminProfileRouteFragment

class MyPageAdminProfileVPAdapter(fragment: Fragment, val gymId: Long) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyPageAdminProfileCommunityFragment()
            1 -> MyPageAdminProfileRouteFragment()
            else -> MyPageAdminProfileInfoFragment(gymId)
        }
    }
}