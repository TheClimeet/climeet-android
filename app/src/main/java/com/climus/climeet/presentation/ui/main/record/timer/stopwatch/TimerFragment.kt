package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordTimerBinding
import com.climus.climeet.presentation.base.BaseFragment

class TimerFragment: BaseFragment<FragmentRecordTimerBinding>(R.layout.fragment_record_timer) {

    private val viewModel: TimerViewModel by viewModels()

    interface OnStartClickListener {
        fun onStartClick()
    }
    var onStartClickListener: OnStartClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        // 시작 버튼 눌림 -> [gone: 시작 버튼] [현재 visible: 정지, 일시정지 버튼]
        binding.ivStart.setOnClickListener {
            onStartClickListener?.onStartClick()    // Indicator 보이기 설정

            it.visibility = View.GONE
            binding.ivStop.visibility = View.VISIBLE
            binding.ivPause.visibility = View.VISIBLE
        }
        // 일시정지 버튼 눌림 -> [gone: 일시정지 버튼] [현재 visible: 정지, 재시작 버튼]
        binding.ivPause.setOnClickListener {
            it.visibility = View.GONE
            binding.ivRestart.visibility = View.VISIBLE
        }
        // 재시작 버튼 눌림 -> [gone: 재시작 버튼] [현재 visible: 정지, 일시정지 버튼]
        binding.ivRestart.setOnClickListener {
            it.visibility = View.GONE
            binding.ivPause.visibility = View.VISIBLE
        }
        // 정지 버튼 눌림 -> [gone: 정지, 재시작, 일시정지 버튼] [현재 visible: 시작 버튼]
        binding.ivStop.setOnClickListener {
            it.visibility = View.GONE
            binding.ivPause.visibility = View.GONE
            binding.ivRestart.visibility = View.GONE
            binding.ivStart.visibility = View.VISIBLE
        }
    }
}