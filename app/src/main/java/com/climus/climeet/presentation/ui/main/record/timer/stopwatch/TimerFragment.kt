package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.NoticePopup
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectBottomFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectionListener
import java.util.concurrent.TimeUnit

enum class ViewMode {
    START, PAUSE, RESTART, STOP
}

class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer),
    CragSelectionListener {

    private val timerVM: TimerViewModel by viewModels()
    private var isStopwatchRunning = false

    interface OnStartClickListener {
        fun onStartClick()
    }

    var onStartClickListener: OnStartClickListener? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = timerVM
        timerVM.registerReceiver(requireContext())
        timerObserve()
        initClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerVM.unregisterReceiver(requireContext())
    }

    private fun timerObserve() {
        timerVM.timeFormat.observe(viewLifecycleOwner, Observer { timeFormat ->
            binding.tvTime.text = timeFormat
        })
    }

    private fun initClickListener() {
        // 암장 선택
        binding.layoutSelectCrag.setOnClickListener {
            if (isStopwatchRunning) {
                NoticePopup.make(it, "운동중에는 암장을 바꿀 수 없어요!").show()
            } else {
                showBottomSheet()
            }
        }

        // 스톱워치 시작
        binding.ivStart.setOnClickListener {
            if (binding.tvTitle.text == getString(R.string.timer_crag_set_inform)) {
                NoticePopup.make(it, "운동 전, 암장을 먼저 선택해주세요.").show()
            } else {
                startStopwatch()    // 스톱워치 서비스 시작
                onStartClickListener?.onStartClick()    // Indicator 보이기 설정
                viewMode(ViewMode.START)
            }
        }

        // 스톱워치 일시정지
        binding.ivPause.setOnClickListener {
            pauseStopwatch()
            viewMode(ViewMode.PAUSE)
        }

        // 스톱워치 재시작
        binding.ivRestart.setOnClickListener {
            restartStopwatch()
            viewMode(ViewMode.RESTART)
        }

        // 정지 버튼 눌림
        binding.ivStop.setOnClickListener {
            NoticePopup.make(it, "정지 버튼을 길게 누르면\n운동이 종료됩니다.").show()
        }

        binding.ivStop.setOnLongClickListener {
            stopStopwatch()
            viewMode(ViewMode.STOP)
            onStartClickListener?.onStartClick()    // Indicator 보이기 설정
            true    // OnClickListener와 같이 쓰기 위해 true로 설정
        }
    }

    private fun startStopwatch() {
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "START")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(intent)
        } else {
            context?.startService(intent)
        }
        isStopwatchRunning = true
    }

    private fun pauseStopwatch() {
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "PAUSE")
        }
        context?.startService(intent)
    }

    private fun restartStopwatch() {
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "RESTART")
        }
        context?.startService(intent)
    }

    private fun stopStopwatch() {
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "STOP")
        }
        context?.startService(intent)
        isStopwatchRunning = false
    }

    override fun onCragSelected(crag: RecordCragData) {
        // 선택된 암장의 이름 보여주기
        binding.tvTitle.text = crag.name
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = CragSelectBottomFragment()
        bottomSheetFragment.cragSelectionListener = this
        bottomSheetFragment.show(parentFragmentManager, "timerCragSelectBottomSheet")
    }

    private fun viewMode(mode: ViewMode) {
        when (mode) {
            ViewMode.START -> {
                // [gone: 시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivStart.visibility = View.GONE
                binding.ivStop.visibility = View.VISIBLE
                binding.ivPause.visibility = View.VISIBLE
            }

            ViewMode.PAUSE -> {
                // [gone: 일시정지 버튼] [현재 visible: 정지, 재시작 버튼]
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.VISIBLE
            }

            ViewMode.RESTART -> {
                // [gone: 재시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivRestart.visibility = View.GONE
                binding.ivPause.visibility = View.VISIBLE

            }

            ViewMode.STOP -> {
                //[gone: 정지, 재시작, 일시정지 버튼] [현재 visible: 시작 버튼]
                binding.ivStop.visibility = View.GONE
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.GONE
                binding.ivStart.visibility = View.VISIBLE
            }
        }
    }
}