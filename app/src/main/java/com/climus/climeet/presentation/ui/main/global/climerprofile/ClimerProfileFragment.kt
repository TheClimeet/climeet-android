package com.climus.climeet.presentation.ui.main.global.climerprofile

import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimerProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager.ClimberProfileVPAdapter
import com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager.MyShortsVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ClimerProfileFragment : BaseFragment<FragmentClimerProfileBinding>(R.layout.fragment_climer_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()

    }

    private fun setupTabLayout() {
        val climberProfileAdapter = ClimberProfileVPAdapter(this)
        binding.vpClimberProfile.adapter = climberProfileAdapter

        val tabMenu = arrayListOf(" 숏츠 ", " 정보 ")
        TabLayoutMediator(binding.tbClimberProfile, binding.vpClimberProfile) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()

    }
}