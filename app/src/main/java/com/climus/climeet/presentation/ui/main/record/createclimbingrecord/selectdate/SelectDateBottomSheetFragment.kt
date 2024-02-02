package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectDateBottomSheetBinding
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.CreateClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.CreateRecordData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

@AndroidEntryPoint
class SelectDateBottomSheetFragment : BottomSheetDialogFragment() {

    private val parentViewModel: CreateClimbingRecordViewModel by activityViewModels()
    private val viewModel: SelectDateBottomSheetViewModel by viewModels()
    private var _binding: FragmentSelectDateBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val today = LocalDate.now()
    private var maxDay = today.lengthOfMonth()
    private val todayYearPos = today.year - 2011
    private val todayMonthPos = today.monthValue - 1
    private val todayDayPos = today.dayOfMonth - 1

    private val yearsArr = Array(90) { i -> "${2011 + i}년" }
    private val monthsArr = Array(12) { i -> "${i + 1}월" }
    private var daysArr = Array(maxDay) { i -> "${i + 1}일" }


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
        setNp()
    }

    private fun initEventObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    SelectDateBottomEvent.CloseFragment -> dismiss()
                    SelectDateBottomEvent.UpdateIsToday -> setNpToday()
                    SelectDateBottomEvent.SetDate -> setDate()
                }
            }
        }
    }

    private fun setDate() {
        CreateRecordData.setSelectedDate(
            LocalDate.of(
                binding.npYear.value + 2011,
                binding.npMonth.value + 1,
                binding.npDay.value + 1
            )
        )

        dismiss()
    }

    private fun setNp() {
        with(binding) {
            npYear.wrapSelectorWheel = true
            npYear.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            npMonth.wrapSelectorWheel = true
            npMonth.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            npDay.wrapSelectorWheel = true
            npDay.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            npYear.minValue = 0
            npMonth.minValue = 0
            npDay.minValue = 0

            npYear.maxValue = yearsArr.size - 1
            npMonth.maxValue = monthsArr.size - 1
            npDay.maxValue = daysArr.size - 1

            npYear.displayedValues = yearsArr
            npMonth.displayedValues = monthsArr
            npDay.displayedValues = daysArr

            setNpToday()

            npYear.setOnValueChangedListener { picker, oldVal, newVal ->
                val selectedYear = yearsArr[newVal]
                if (oldVal != newVal) {
                    viewModel.setIsTodayToFalse()
                    updateDayPicker(selectedYear.dropLast(1).toInt(), npMonth.value + 1)
                }
            }
            npMonth.setOnValueChangedListener { picker, oldVal, newVal ->
                val selectedMonth = monthsArr[newVal]
                if (oldVal != newVal) {
                    viewModel.setIsTodayToFalse()
                    updateDayPicker(npYear.value + 2011, selectedMonth.dropLast(1).toInt())
                }
            }
            npDay.setOnValueChangedListener { picker, oldVal, newVal ->
                if (oldVal != newVal) {
                    viewModel.setIsTodayToFalse()
                }
            }
        }
    }

    private fun setNpToday() {
        with(binding) {
            npYear.value = todayYearPos
            npMonth.value = todayMonthPos
            npDay.value = todayDayPos
        }
    }

    private fun updateDayPicker(year: Int, month: Int) {
        with(binding) {
            val curDay = npDay.value
            maxDay = LocalDate.of(year, month, 1).lengthOfMonth()
            npDay.displayedValues = null
            npDay.maxValue = maxDay - 1
            npDay.displayedValues = Array(maxDay) { i -> "${i + 1}일" }
            if (curDay >= maxDay) {
                npDay.value = maxDay - 1
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (CreateRecordData.selectedDate.year != 9999) {
            parentViewModel.setSelectedDate(CreateRecordData.selectedDate)
        }
    }

}