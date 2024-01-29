package com.climus.climeet.presentation.ui.main.record.timer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerViewPagerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerMainFragment : BaseFragment<FragmentTimerViewPagerBinding>(R.layout.fragment_timer_view_pager) {

    private val viewModel: TimerMainViewModel by viewModels()
    private val timerVM: TimerViewModel by viewModels()
    private lateinit var timerMainAdapter: TimerMainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setViewpager()
        setObserver()
    }

    private fun setViewpager() {
        timerMainAdapter = TimerMainAdapter(this, timerVM)
        binding.vpTimer.adapter = timerMainAdapter
        binding.vpTimer.offscreenPageLimit = 1 // 현재 페이지를 기준으로 좌우 1개 페이지를 메모리에 유지 (시간 유지)

        binding.vpTimer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}
        })
    }

    private fun setObserver() {
        timerVM.isRunning.observe(viewLifecycleOwner) { stopwatchState ->
            if (stopwatchState) {
                binding.idcTimer.visibility = View.VISIBLE
                binding.idcTimer.setViewPager(binding.vpTimer) // indicator 연결
                Log.d("timer", "indicator 연결")
            } else {
                binding.idcTimer.visibility = View.INVISIBLE
                Log.d("timer", "indicator 연결 안 됨")
            }
        }
    }
}