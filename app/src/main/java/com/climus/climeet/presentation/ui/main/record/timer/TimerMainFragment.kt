package com.climus.climeet.presentation.ui.main.record.timer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentTimerViewPagerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerMainFragment :
    BaseFragment<FragmentTimerViewPagerBinding>(R.layout.fragment_timer_view_pager) {

    private val viewModel: TimerMainViewModel by viewModels()
    private val timerVM: TimerViewModel by activityViewModels()
    private lateinit var timerMainAdapter: TimerMainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setViewPager()
        setObserver()
    }

    fun goToStopwatch() {
        binding.vpTimer.setCurrentItem(0, true)
    }

    override fun onResume() {
        super.onResume()
        if (timerVM.isRunning.value == true) {
            binding.idcTimer.visibility = View.VISIBLE
            binding.vpTimer.isUserInputEnabled = true   // 화면 넘길 수 있음
            Log.d("timer", "[메인 프레그먼트 onResume] indicator 보임")
        } else {
            binding.idcTimer.visibility = View.INVISIBLE
            binding.vpTimer.isUserInputEnabled = false  // 화면 넘길 수 없음
            Log.d("timer", "[메인 프레그먼트 onResume] indicator 안 보임")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerVM.unregisterReceiver(requireContext())
        Log.d("timer", "루트 기록 unregistered")
    }

    private fun setViewPager() {
        timerMainAdapter = TimerMainAdapter(this)
        binding.vpTimer.adapter = timerMainAdapter
        binding.vpTimer.offscreenPageLimit = 1
        binding.idcTimer.setViewPager(binding.vpTimer)

        binding.vpTimer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 루트 기록 화면에서는 indicator를 안 보여준다
                when (position) {
                    0 -> binding.idcTimer.visibility = View.VISIBLE
                    1 -> binding.idcTimer.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setObserver() {
        timerVM.isRunning.observe(viewLifecycleOwner) { stopwatchState ->
            if (stopwatchState) {
                binding.idcTimer.visibility = View.VISIBLE
                binding.vpTimer.isUserInputEnabled = true   // 화면 넘길 수 있음
                Log.d("timer", "indicator 보임")
            } else {
                binding.idcTimer.visibility = View.INVISIBLE
                binding.vpTimer.isUserInputEnabled = false  // 화면 넘길 수 없음
                Log.d("timer", "indicator 안 보임")
            }
        }
    }
}