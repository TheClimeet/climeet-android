package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimberProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClimberProfileInfoFragment : BaseFragment<FragmentClimberProfileInfoBinding>(R.layout.fragment_climber_profile_info) {


    private val viewModel: ClimberProfileInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
    }
}