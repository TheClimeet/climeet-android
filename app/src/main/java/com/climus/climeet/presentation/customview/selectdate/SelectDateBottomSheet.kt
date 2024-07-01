package com.climus.climeet.presentation.customview.selectdate

import android.content.Context
import android.widget.Toast
import com.climus.climeet.databinding.FragmentSelectDateBottomSheetBinding
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate

class SelectDateBottomSheet(
    context: Context,
    private val viewModel: SelectDateBottomSheetViewModel,
    private var curDate: LocalDate,
    private var changeDate: (LocalDate) -> Unit,
    private val setSelectedDate: (LocalDate) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var binding: FragmentSelectDateBottomSheetBinding
    init{
        setOnShowListener {
            initView()
        }
    }

    private fun initView() {
        binding = FragmentSelectDateBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initEventObserve()
        setDatePicker(curDate)

        viewModel.setIsTodayToFalse()

        binding.tvOk.setOnClickListener {
            val date = LocalDate.of(
                binding.datepicker.year,
                binding.datepicker.month + 1,
                binding.datepicker.dayOfMonth
            )
            if(date.isAfter(LocalDate.now())) {
                Toast.makeText(context, "미래의 날짜는 선택할 수 없습니다!", Toast.LENGTH_SHORT).show()
            }else{
                setDate(date)
            }
        }
    }

    private fun initEventObserve() {
        viewModel.event.onEach { event ->
            when (event) {
                SelectDateBottomEvent.CloseFragment -> dismiss()
                SelectDateBottomEvent.UpdateIsToday -> setDatePicker(LocalDate.now())
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun setDate(updateDate: LocalDate) {
        changeDate(updateDate)
        setSelectedDate(updateDate)
        dismiss()
    }

    private fun setDatePicker(date: LocalDate) {
        with(binding) {
            datepicker.setOnDateChangedListener(null)
            datepicker.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)
            datepicker.setOnDateChangedListener { _, _, _, _ ->
                viewModel.setIsTodayToFalse()
            }
        }
    }
}