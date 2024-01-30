package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCreateClimbingRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate.SelectDateBottomFragment
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate.SharedBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selecttime.SelectTimeBottomFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateClimbingRecordFragment :
    BaseFragment<FragmentCreateClimbingRecordBinding>(R.layout.fragment_create_climbing_record) {

    private val viewModel: CreateClimbingRecordViewModel by viewModels()
    private lateinit var sharedModel: SharedBottomSheetViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        sharedModel = ViewModelProvider(requireActivity()).get(SharedBottomSheetViewModel::class.java)
        sharedModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
           viewModel.setDate(date)
            binding.tvChoiceDate.setTextColor(Color.WHITE)
        })

        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    CreateClimbingRecordEvent.ShowDatePicker -> showDatePicker()
                    CreateClimbingRecordEvent.ShowTimePicker -> showTimePicker()
                    CreateClimbingRecordEvent.NavigateToSelectCrag -> findNavController().toSelectCrag()
                }
            }
        }
    }

    private fun showDatePicker() {
        val selectDateBottomFragment = SelectDateBottomFragment()
        selectDateBottomFragment.show(parentFragmentManager, "SelectDatePickerBottomSheet")
    }

    private fun showTimePicker() {
        val selectTimeBottomFragment = SelectTimeBottomFragment()
        selectTimeBottomFragment.show(parentFragmentManager, "SelectTimePickerBottomSheet")
    }

    private fun NavController.toSelectCrag() {
        val action =
            CreateClimbingRecordFragmentDirections.actionCreateClimbingRecordFragmentToCreateSelectCragFragment()
        navigate(action)
    }

}