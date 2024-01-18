package com.climus.climeet.presentation.ui.main.record

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordTimerBinding
import com.climus.climeet.presentation.base.BaseFragment

class RecordTimerFragment: BaseFragment<FragmentRecordTimerBinding>(R.layout.fragment_record_timer) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpager()
    }

    private fun setViewpager() {
        // indicator 보여지기 설정
        val adapter = RecordTimerAdapter().apply {
            indicatorVisibilityListener = {
                if (binding.idcTimer.visibility == View.VISIBLE) {
                    binding.idcTimer.visibility = View.INVISIBLE
                } else {
                    binding.idcTimer.visibility = View.VISIBLE
                }
            }
        }

        binding.vpTimer.adapter = adapter
        binding.idcTimer.setViewPager(binding.vpTimer)

        binding.vpTimer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}
        })
    }


}