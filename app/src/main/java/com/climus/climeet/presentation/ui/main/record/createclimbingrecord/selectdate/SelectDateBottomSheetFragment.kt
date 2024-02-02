package com.climus.climeet.presentation.ui.main.record.createclimbingrecord.selectdate

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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

    private val today = LocalDate.now()
    private val minYear = 2011
//    private var maxDay = LocalDate.of(today.year, today.month, 1).lengthOfMonth()
    private var maxDay = 31

    // 오늘 날짜의 포지션
    private val curYear = 90 * 10000 + today.year - minYear - 2
    private val curMonth = 12 * 10000 + today.monthValue + 9
    private var curDay = maxDay * 10000 + today.dayOfMonth + maxDay - 3

    // 설정된 날짜의 포지션
    private var yearPosition = curYear + 10
    private var monthPosition = curMonth + 10
    private var dayPosition = curDay + 10

    // 선택된 실제 날짜
    private var selectedYear = today.year
    private var selectedMonth = today.monthValue
    private var selectedDay = today.dayOfMonth

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

    private fun setRV() {
        updateDateToday()

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
    }

    private fun setDate() {
        CreateRecordData.setSelectedDate(
            LocalDate.of(
                selectedYear,
                selectedMonth,
                selectedDay
            )
        )

        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (CreateRecordData.selectedDate.year != 9999) {
            parentViewModel.setSelectedDate(CreateRecordData.selectedDate)
        }
    }

    private fun updateDateToday() {
        with(binding) {
            yearAdapter = SelectDateAdapter((2011..2100).map { "${it}년" })
            monthAdapter = SelectDateAdapter((1..12).map { "${it}월" })
            dayAdapter = SelectDateAdapter((1..31).map { "${it}일" })
            rvYear.adapter = yearAdapter
            rvMonth.adapter = monthAdapter
            rvDay.adapter = dayAdapter

            rvYear.scrollToPosition(curYear)
            rvMonth.scrollToPosition(curMonth)
            rvDay.scrollToPosition(curDay)

            val snapHelperYear = LinearSnapHelper()
            val snapHelperMonth = LinearSnapHelper()
            val snapHelperDay = LinearSnapHelper()

            rvYear.onFlingListener = null
            rvMonth.onFlingListener = null
            rvDay.onFlingListener = null
            rvYear.post {
                snapHelperYear.attachToRecyclerView(rvYear)
            }
            rvMonth.post {
                snapHelperMonth.attachToRecyclerView(rvMonth)
            }
            rvDay.post {
                snapHelperDay.attachToRecyclerView(rvDay)
            }

            rvYear.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        yearPosition = position
                        if (yearPosition != curYear || monthPosition != curMonth || dayPosition != curDay) {
                            viewModel.setIsTodayToFalse()
                            selectedYear = yearPosition % 90 + 2013
                        }
                    }
                }
            })
            rvMonth.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        monthPosition = position
                        if (yearPosition != curYear || monthPosition != curMonth || dayPosition != curDay) {
                            viewModel.setIsTodayToFalse()
                            selectedMonth = ((monthPosition - 9) % 12).takeIf { it != 0 } ?: 12
                        }
                    }
                }
            })
            rvDay.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        dayPosition = position
                        if (yearPosition != curYear || monthPosition != curMonth || dayPosition != curDay) {
                            viewModel.setIsTodayToFalse()
                            selectedDay = (dayPosition % maxDay) + 3
                        }
                    }
                }
            })
        }
    }
}