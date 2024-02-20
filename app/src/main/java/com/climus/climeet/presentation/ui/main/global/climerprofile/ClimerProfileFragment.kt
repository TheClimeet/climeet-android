package com.climus.climeet.presentation.ui.main.global.climerprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimerProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager.ClimberProfileVPAdapter
import com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager.MyShortsVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClimerProfileFragment : BaseFragment<FragmentClimerProfileBinding>(R.layout.fragment_climer_profile) {

    private val args : ClimerProfileFragmentArgs by navArgs()
    private val userId by lazy { args.userId }

    private val viewModel : ClimberProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setUserId(userId)
        setupTabLayout()
        initStateObserve()

    }

    private fun setupTabLayout() {
        val climberProfileAdapter = ClimberProfileVPAdapter(this, userId)
        binding.vpClimberProfile.adapter = climberProfileAdapter

        val tabMenu = arrayListOf(" 숏츠 ", " 정보 ")
        TabLayoutMediator(binding.tbClimberProfile, binding.vpClimberProfile) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()

    }

    private fun initStateObserve(){
        repeatOnStarted {
            viewModel.uiState.collect{
                if(it.isFollower){
                    binding.btnFollow.visibility = View.INVISIBLE
                    binding.btnFollowing.visibility = View.VISIBLE
                } else {
                    binding.btnFollow.visibility = View.VISIBLE
                    binding.btnFollowing.visibility = View.INVISIBLE
                }
            }
        }
    }
}