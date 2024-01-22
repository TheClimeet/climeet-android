package com.climus.climeet.presentation.ui.main.record.timer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerVpBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel

class RecordTimerFragment: BaseFragment<FragmentTimerVpBinding>(R.layout.fragment_timer_vp),
    TimerFragment.OnStartClickListener {

    private lateinit var recordTimerAdapter: RecordTimerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewpager()
    }

    private fun setViewpager() {
        recordTimerAdapter = RecordTimerAdapter(this, this)
        binding.vpTimer.adapter = recordTimerAdapter
        binding.vpTimer.offscreenPageLimit = 1 // 현재 페이지를 기준으로 좌우 1개 페이지를 메모리에 유지 (시간 유지)

        binding.vpTimer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}
        })
    }

    override fun onStartClick() {
        if (binding.idcTimer.visibility == View.VISIBLE) {
            binding.idcTimer.visibility = View.INVISIBLE
            recordTimerAdapter.setTimerStarted(false)
            Log.d("timer", "indicator invisible")
        } else {
            binding.idcTimer.visibility = View.VISIBLE
            recordTimerAdapter.setTimerStarted(true)
            binding.idcTimer.setViewPager(binding.vpTimer) // indicator 연결
            Log.d("timer", "indicator visible")
        }
    }
}