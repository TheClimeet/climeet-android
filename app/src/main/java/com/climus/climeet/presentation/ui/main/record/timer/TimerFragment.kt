package com.climus.climeet.presentation.ui.main.record.timer

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

        // 시작 버튼 눌림
        binding.ivStart.setOnClickListener {
            onStartClickListener?.onStartClick()
        }
    }
}