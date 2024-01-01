package com.climus.climeet.presentation.ui.intro.onboard

import android.os.Bundle
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentOnboardBinding
import com.climus.climeet.presentation.base.BaseFragment

class OnBoardFragment : BaseFragment<FragmentOnboardBinding>(R.layout.fragment_onboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpOnboard.adapter =
            OnBoardAdapter(listOf("흩어져 있는 클라이밍 루트 정보", "어려웠던 클라이밍 운동 기록", "혼클할 때 서러웠던 루트 파인딩"))
        binding.idcOnboard.setViewPager(binding.vpOnboard)
    }
}