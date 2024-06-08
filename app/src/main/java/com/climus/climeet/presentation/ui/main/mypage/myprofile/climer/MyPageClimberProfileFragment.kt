package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageClimberProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.climerprofile.ClimberProfileViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager.MyPageClimberProfileVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageClimberProfileFragment :
    BaseFragment<FragmentMypageClimberProfileBinding>(R.layout.fragment_mypage_climber_profile) {

    private val sharedViewModel: ClimberProfileViewModel by activityViewModels()

    private val args: MyPageClimberProfileFragmentArgs by navArgs()
    private val userId by lazy { args.userId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        sharedViewModel.setUserId(userId)
        setupTabLayout()
    }

    private fun setupTabLayout() {
        val myPageClimberProfileAdapter = MyPageClimberProfileVPAdapter(this, userId)
        binding.vpClimberProfile.adapter = myPageClimberProfileAdapter

        val tabMenu = arrayListOf(" 숏츠 ", " 정보 ")
        TabLayoutMediator(binding.tbClimberProfile, binding.vpClimberProfile) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()

    }
}