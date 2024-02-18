package com.climus.climeet.presentation.ui.main.mypage.myshorts

import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageMyshortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowVPAdapter
import com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager.MyShortsVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMyShortsFragment: BaseFragment<FragmentMypageMyshortsBinding>(R.layout.fragment_mypage_myshorts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()

    }

    private fun setupTabLayout() {
        val shortsAdapter = MyShortsVPAdapter(this)
        binding.vpMyapgeShorts.adapter = shortsAdapter

        val tabMenu = arrayListOf(" 좋아요 ", " 댓글 ", " 저장 ")
        TabLayoutMediator(binding.tbMypageShorts, binding.vpMyapgeShorts) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()

    }

}