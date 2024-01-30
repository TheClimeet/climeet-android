package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSelectDateBottomSheetBinding
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.CreateClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.CreateRecordData
import com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate.adpater.SelectDateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class SelectDateBottomSheetFragment : BottomSheetDialogFragment() {

    private val parentViewModel: CreateClimbingRecordViewModel by activityViewModels()
    private val viewModel: SelectDateBottomSheetViewModel by viewModels()
    private var _binding: FragmentSelectDateBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var yearAdapter: SelectDateAdapter? = null
    private var monthAdapter: SelectDateAdapter? = null
    private var dayAdapter: SelectDateAdapter? = null

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

        setRV()

        initDatePicker()
        initEventObserve()

    }

    private fun initEventObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    SelectDateBottomEvent.CloseFragment -> dismiss()
                    SelectDateBottomEvent.UpdateIsToday -> updateDateToday()
                    SelectDateBottomEvent.SetDate -> setDate()
                }
            }
        }
    }

    private fun setRV(){
        yearAdapter = SelectDateAdapter((2010..2099).map{"${it}년"})
        monthAdapter = SelectDateAdapter((1..12).map{"${it}월"})
        dayAdapter = SelectDateAdapter((1..31).map{"${it}일"})
        binding.rvYear.adapter = yearAdapter
        binding.rvMonth.adapter = monthAdapter
        binding.rvDay.adapter = dayAdapter

        // 독립적인 스크롤을 위한 코드
        val onTouchListener = object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

        binding.rvYear.addOnItemTouchListener(onTouchListener)
        binding.rvMonth.addOnItemTouchListener(onTouchListener)
        binding.rvDay.addOnItemTouchListener(onTouchListener)

        val snapHelperYear = LinearSnapHelper()
        val snapHelperMonth = LinearSnapHelper()
        val snapHelperDay = LinearSnapHelper()
        snapHelperYear.attachToRecyclerView(binding.rvYear)
        snapHelperMonth.attachToRecyclerView(binding.rvMonth)
        snapHelperDay.attachToRecyclerView(binding.rvDay)
    }

    private fun setDate() {
//        CreateRecordData.setSelectedDate(
//            LocalDate.of(
//                binding.dpDate.year,
//                binding.dpDate.month + 1,
//                binding.dpDate.dayOfMonth
//            )
//        )
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(CreateRecordData.selectedDate.year != 9999){
            parentViewModel.setSelectedDate(CreateRecordData.selectedDate)
        }
    }

    private fun updateDateToday() {
        val today = LocalDate.now()
//        binding.dpDate.updateDate(today.year, today.monthValue - 1, today.dayOfMonth)
    }

    private fun initDatePicker() {
        if (CreateRecordData.selectedDate?.year != 9999) {
            setDatePicker(CreateRecordData.selectedDate)
        } else {
            setDatePicker(LocalDate.now())
        }
    }

    private fun setDatePicker(calendar: LocalDate) {
        val year = calendar.year
        val month = calendar.monthValue - 1
        val day = calendar.dayOfMonth
//        binding.dpDate.init(
//            year,
//            month,
//            day,
//            DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
//                if (viewModel.isToday.value) {
//                    viewModel.updateIsToday()
//                }
//
//            })
    }

}