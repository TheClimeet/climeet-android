package com.climus.climeet.presentation.ui.main.record.stats.datepicker

import android.content.Context
import android.widget.NumberPicker
import com.climus.climeet.databinding.FragmentSelectYearmonthBottomSheetBinding
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate

class SelectYearMonthBottomSheet(
    context: Context,
    private var curDate: LocalDate,
    private var changeDate: (LocalDate) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var binding: FragmentSelectYearmonthBottomSheetBinding

    private val curYearPos = curDate.year - 1900
    private val curMonthPos = curDate.monthValue - 1
    private val yearsArr = Array(201) { i -> "${1900 + i}" }
    private val monthsArr = Array(12) { i -> "${i + 1}" }

    init {
        setOnShowListener {
            initView()
        }
    }

    private fun initView() {
        binding = FragmentSelectYearmonthBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDatePicker()

        with(binding){
            ivClose.setOnClickListener {
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvOk.setOnClickListener {
                setDate()
            }
        }
    }

    private fun setDate() {
        val date = LocalDate.of(
            binding.npYear.value + 1900,
            binding.npMonth.value + 1,
            1
        )
        changeDate(date)
        dismiss()
    }

    private fun setDatePicker() {
        with(binding) {
            npYear.wrapSelectorWheel = true
            npYear.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            npMonth.wrapSelectorWheel = true
            npMonth.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            npYear.minValue = 0
            npMonth.minValue = 0

            npYear.maxValue = yearsArr.size - 1
            npMonth.maxValue = monthsArr.size - 1

            npYear.displayedValues = yearsArr
            npMonth.displayedValues = monthsArr

            npYear.value = curYearPos
            npMonth.value = curMonthPos

        }
    }
}