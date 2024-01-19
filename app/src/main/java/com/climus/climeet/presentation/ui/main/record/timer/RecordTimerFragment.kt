package com.climus.climeet.presentation.ui.main.record.timer

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordTimerVpBinding
import com.climus.climeet.presentation.base.BaseFragment

class RecordTimerFragment: BaseFragment<FragmentRecordTimerVpBinding>(R.layout.fragment_record_timer_vp),
    TimerFragment.OnStartClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpager()
    }

    private fun setViewpager() {
        binding.vpTimer.adapter = RecordTimerAdapter(this, this)
        binding.idcTimer.setViewPager(binding.vpTimer)
        binding.vpTimer.offscreenPageLimit = 1 // 현재 페이지를 기준으로 좌우 1개 페이지를 메모리에 유지

        binding.vpTimer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}
        })
    }

    override fun onStartClick() {
        if (binding.idcTimer.visibility == View.VISIBLE) {
            binding.idcTimer.visibility = View.INVISIBLE
        } else {
            binding.idcTimer.visibility = View.VISIBLE
        }
    }
}