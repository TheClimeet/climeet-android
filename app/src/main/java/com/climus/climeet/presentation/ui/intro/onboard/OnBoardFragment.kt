package com.climus.climeet.presentation.ui.intro.onboard

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentOnboardBinding
import com.climus.climeet.presentation.base.BaseFragment

class OnBoardFragment : BaseFragment<FragmentOnboardBinding>(R.layout.fragment_onboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpager()
        setBtnListener()
    }

    private fun setViewpager() {
        binding.vpOnboard.adapter =
            OnBoardAdapter(
                listOf(
                    OnBoardItem(
                        "클밋에서 만나다.",
                        "흩어져 있는 클라임이 루트정보",
                        R.drawable.ic_onboard_1
                    ),
                    OnBoardItem(
                        "루트별로 만나다.",
                        "매일 성장하는 나의 기록",
                        R.drawable.ic_onboard_2
                    ),
                    OnBoardItem(
                        "나를 성장시키는 통계까지.",
                        "함께 성장하는 클라이밍 커뮤니티",
                        R.drawable.ic_onboard_3
                    )
                )
            )
        binding.idcOnboard.setViewPager(binding.vpOnboard)
    }

    private fun setBtnListener() {
        binding.btnStart.setOnClickListener {
            findNavController().toLogin()
        }
    }

    private fun NavController.toLogin() {
        val action = OnBoardFragmentDirections.actionOnboardFragmentToLoginFragment()
        navigate(action)
    }
}