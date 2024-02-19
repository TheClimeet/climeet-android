package com.climus.climeet.presentation.ui.main.mypage.announce

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAnnounceBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.HomeFragmentDirections
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.FollowVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MyPageAnnounceFragment: BaseFragment<FragmentMypageAnnounceBinding>(R.layout.fragment_mypage_announce) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListenr()

    }

    private fun setupClickListenr() {
        binding.tvAnnounceBox.setOnClickListener {
            navigateToAnnounceDetail()
        }
    }

    private fun navigateToAnnounceDetail() {
        val action = MyPageAnnounceFragmentDirections.actionMyPageAnnounceFragmentToAnnounceDetailFragment()
        findNavController().navigate(action)
    }

}