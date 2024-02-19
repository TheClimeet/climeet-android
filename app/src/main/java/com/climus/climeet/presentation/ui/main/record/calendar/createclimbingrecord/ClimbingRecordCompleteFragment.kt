package com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimbingRecordCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarViewModel
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData


class ClimbingRecordCompleteFragment :
    BaseFragment<FragmentClimbingRecordCompleteBinding>(R.layout.fragment_climbing_record_complete) {
    private val viewModel: CalendarViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCheck.setImageResource(R.drawable.ic_check_anim)
        val drawable = binding.ivCheck.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }
        if (viewModel.selectedDate.value == CreateRecordData.selectedDate) {
            viewModel.setRecord(CreateRecordData.selectedDate)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().toCalendarFragment()
        }, 1000)
    }

    private fun NavController.toCalendarFragment() {
        val action =
            ClimbingRecordCompleteFragmentDirections.actionFragmentClimbingRecordCompleteToRecordFragment()
        navigate(action)
    }
}