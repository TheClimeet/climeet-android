package com.climus.climeet.presentation.ui.main.mypage.follow

import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageFollowBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.viewpager.best.RankingVPAdapter
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFollowFragment: BaseFragment<FragmentMypageFollowBinding>(R.layout.fragment_mypage_follow) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()

    }

    private fun setupTabLayout() {
        val followAdapter = FollowVPAdapter(this)
        binding.vpFollowFollower.adapter = followAdapter

        val tabMenu = arrayListOf(" 팔로워 ", " 팔로잉 ")
        TabLayoutMediator(binding.tbMypageFollow, binding.vpFollowFollower) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

}