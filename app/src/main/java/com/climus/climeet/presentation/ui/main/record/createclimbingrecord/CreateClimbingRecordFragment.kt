package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCreateClimbingRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate.SelectDateBottomFragment
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime.SelectTimeBottomFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateClimbingRecordFragment :
    BaseFragment<FragmentCreateClimbingRecordBinding>(R.layout.fragment_create_climbing_record) {

    private val viewModel: CreateClimbingRecordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    CreateClimbingRecordEvent.ShowDatePicker -> showDatePicker()
                    CreateClimbingRecordEvent.ShowTimePicker -> showTimePicker()
                }
            }
        }
    }

    private fun showDatePicker() {
        val selectDateBottomFragment = SelectDateBottomFragment()
        selectDateBottomFragment.show(parentFragmentManager, "SelectDatePickerBottomSheet")
        Log.d("dateTest", "fr ${CreateRecordData.selectedDate}")
        if (CreateRecordData.selectedDate?.year != 9999) {
            viewModel.setDate()
            binding.tvChoiceDate.setTextColor(Color.WHITE)
        }
    }

    private fun showTimePicker(){
        val selectTimeBottomFragment = SelectTimeBottomFragment()
        selectTimeBottomFragment.show(parentFragmentManager, "SelectTimePickerBottomSheet")
    }

}