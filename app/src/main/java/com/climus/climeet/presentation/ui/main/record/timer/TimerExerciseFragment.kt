package com.climus.climeet.presentation.ui.main.record.timer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordTimerExerciseBinding
import com.climus.climeet.presentation.base.BaseFragment

class TimerExerciseFragment: BaseFragment<FragmentRecordTimerExerciseBinding>(R.layout.fragment_record_timer_exercise) {

    private val viewModel: TimerExerciseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩 변수에 뷰모델 연결
        binding.vm = viewModel


    }
}