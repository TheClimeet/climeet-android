package com.climus.climeet.presentation.ui.main.record.timer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerViewPagerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

// --------------------------- 스톱워치, 루트기록 화면을 담는 뷰페이저 -----------------------------
@AndroidEntryPoint
class TimerMainFragment :
    BaseFragment<FragmentTimerViewPagerBinding>(R.layout.fragment_timer_view_pager) {

    private val viewModel: TimerMainViewModel by activityViewModels()
    private val timerVM: TimerViewModel by activityViewModels()
    private lateinit var timerMainAdapter: TimerMainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setViewPager()
        setObserver()
        moveToTimerFragment()
    }

    // indicator 보이기 설정
    override fun onResume() {
        super.onResume()
        if (timerVM.isRunning.value == true) {
            binding.idcTimer.visibility = View.VISIBLE
            binding.vpTimer.isUserInputEnabled = true   // 화면 넘길 수 있음
        } else {
            binding.idcTimer.visibility = View.INVISIBLE
            binding.vpTimer.isUserInputEnabled = false  // 화면 넘길 수 없음
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerVM.unregisterReceiver(requireContext())
    }

    private fun setViewPager() {
        timerMainAdapter = TimerMainAdapter(this)
        binding.vpTimer.adapter = timerMainAdapter
        binding.vpTimer.offscreenPageLimit = 1
        binding.idcTimer.setViewPager(binding.vpTimer)

        binding.vpTimer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 0번에 연결된 스톱워치 화면에서 스톱워치가 running 상태일 때만 indicator를 보여준다
                if (position == 0) {
                    if (timerVM.isRunning.value == true) {
                        binding.idcTimer.visibility = View.VISIBLE
                    }
                } else {
                    binding.idcTimer.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setObserver() {
        timerVM.isRunning.observe(viewLifecycleOwner) { stopwatchState ->
            if (stopwatchState) {
                binding.idcTimer.visibility = View.VISIBLE
                binding.vpTimer.isUserInputEnabled = true   // 화면 넘길 수 있음
            } else {
                binding.idcTimer.visibility = View.INVISIBLE
                binding.vpTimer.isUserInputEnabled = false  // 화면 넘길 수 없음
            }
        }
    }

    // 루트기록 화면에서 시간을 누르면 스톱워치 화면으로 이동한다 (TimerFragment)
    private fun moveToTimerFragment() {
        viewModel.moveToTimerFragmentEvent.observe(viewLifecycleOwner, Observer {
            binding.vpTimer.currentItem = 0
            Log.d("move", "메인에서 화면 이동시켜야함")
        })
    }
}