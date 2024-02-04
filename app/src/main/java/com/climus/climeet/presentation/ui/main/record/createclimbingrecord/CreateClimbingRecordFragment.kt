package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCreateClimbingRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.toSelectDateBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateClimbingRecordFragment :
    BaseFragment<FragmentCreateClimbingRecordBinding>(R.layout.fragment_create_climbing_record) {

    private val viewModel: CreateClimbingRecordViewModel by activityViewModels()
    private var isTimeSet = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            viewModel.setDate()
            binding.tvChoiceDate.setTextColor(Color.WHITE)
        })
        viewModel.selectedStartTime.observe(viewLifecycleOwner, Observer { date ->
            if (isTimeSet) {
                viewModel.setTime()
                binding.tvChoiceTime.setTextColor(Color.WHITE)
            }
        })
        viewModel.selectedEndTime.observe(viewLifecycleOwner, Observer { date ->
            if (isTimeSet) {
                viewModel.setTime()
                binding.tvChoiceTime.setTextColor(Color.WHITE)
            }
        })

        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    CreateClimbingRecordEvent.ShowDatePicker -> findNavController().toSelectDateBottomSheetFragment()
                    CreateClimbingRecordEvent.ShowTimePicker -> {
                        isTimeSet = true
                        findNavController().toSelectTimeBottomSheetFragment()
                    }
                    CreateClimbingRecordEvent.NavigateToSelectCrag -> findNavController().toSelectCrag()
                }
            }
        }
    }

    private fun NavController.toSelectTimeBottomSheetFragment() {
        val action =
            CreateClimbingRecordFragmentDirections.actionCreateClimbingRecordFragmentToSelectTimeBottomFragment()
        navigate(action)
    }

    private fun NavController.toSelectCrag() {
        val action =
            CreateClimbingRecordFragmentDirections.actionCreateClimbingRecordFragmentToCreateSelectCragFragment()
        navigate(action)
    }

}