package com.climus.climeet.presentation.ui.intro.onboard

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
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
            OnBoardAdapter(listOf("흩어져 있는 클라이밍 루트 정보", "어려웠던 클라이밍 운동 기록", "혼클할 때 서러웠던 루트 파인딩"))
        binding.idcOnboard.setViewPager(binding.vpOnboard)

        binding.vpOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (binding.vpOnboard.currentItem == binding.vpOnboard.adapter?.itemCount?.minus(1)) {
                    binding.btnStart.visibility = View.VISIBLE
                } else {
                    binding.btnStart.visibility = View.INVISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}
        })
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