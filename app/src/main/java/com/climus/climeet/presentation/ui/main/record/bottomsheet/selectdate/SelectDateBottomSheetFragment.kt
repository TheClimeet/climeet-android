package com.climus.climeet.presentation.ui.main.record.bottomsheet.selectdate

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectDateBottomSheetBinding
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarViewModel
import com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord.CreateClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class SelectDateBottomSheetFragment : BottomSheetDialogFragment() {

    private val recordViewModel: CreateClimbingRecordViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private val viewModel: SelectDateBottomSheetViewModel by viewModels()
    private var _binding: FragmentSelectDateBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val today = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_date_bottom_sheet,
            container,
            false
        )
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEventObserve()
        setDatePicker(CreateRecordData.selectedDate)
    }

    private fun initEventObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    SelectDateBottomEvent.CloseFragment -> dismiss()
                    SelectDateBottomEvent.UpdateIsToday -> setDatePicker(today)
                    SelectDateBottomEvent.SetDate -> setDate()
                }
            }
        }
    }

    private fun setDate() {
        CreateRecordData.setSelectedDate(
            LocalDate.of(
                binding.datepicker.year,
                binding.datepicker.month + 1,
                binding.datepicker.dayOfMonth
            )
        )
        dismiss()
    }

    private fun setDatePicker(date: LocalDate) {
        with(binding) {
            datepicker.setOnDateChangedListener(null)
            datepicker.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)
            datepicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                viewModel.setIsTodayToFalse()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        recordViewModel.setSelectedDate(CreateRecordData.selectedDate)
        calendarViewModel.setSelectedDate(CreateRecordData.selectedDate)
    }

}