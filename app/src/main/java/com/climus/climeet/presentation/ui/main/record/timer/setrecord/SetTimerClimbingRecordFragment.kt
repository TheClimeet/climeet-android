package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerRecordBinding
import com.climus.climeet.presentation.base.BaseFragment

class SetTimerClimbingRecordFragment: BaseFragment<FragmentTimerRecordBinding>(R.layout.fragment_timer_record) {

    private val viewModel: SetTimerClimbingRecordViewmodel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel


    }
}