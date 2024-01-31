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
    private val mid = Int.MAX_VALUE / 2
    private val curYear = mid + today.year - 2075
    private val curMonth = mid + today.monthValue - 6
    private val curDay = mid + today.dayOfMonth - 3
    private var yearPosition = 0
    private var monthPosition = 0
    private var dayPosition = 0

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
        if (CreateRecordData.selectedDate.year != 9999) {
            parentViewModel.setSelectedDate(CreateRecordData.selectedDate)
        }
    }

    private fun updateDateToday() {
        with(binding){
            // mid만 넣으면 2073년 4월 1일이 됨
            // year은 63빼고, month는 3뺌
            // 하지만 가운데로 하기 위해서는 -2를 해줘야함
            // 따라서 year은 mid+today.year-2071
            // month는 mid+today.monthValue-2
            // day는 mid+today.dayOfMonth+1
            yearAdapter = SelectDateAdapter((2010..2099).map { "${it}년" })
            monthAdapter = SelectDateAdapter((1..12).map { "${it}월" })
            dayAdapter = SelectDateAdapter((1..31).map { "${it}일" })
            rvYear.adapter = yearAdapter
            rvMonth.adapter = monthAdapter
            rvDay.adapter = dayAdapter

            rvYear.scrollToPosition(curYear)
            rvMonth.scrollToPosition(curMonth)
            rvDay.scrollToPosition(curDay)

            Log.d("dateTest", "연도 $curYear")
            Log.d("dateTest", "달 $curMonth")
            Log.d("dateTest", "일 $curDay")



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

            rvYear.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        yearPosition = position
                        Log.d("dateTest", "년position $yearPosition")
                        if(yearPosition != curYear || monthPosition != curMonth || dayPosition != curDay){
                            viewModel.setIsTodayToFalse()
                        }
                    }
                }
            })
            rvMonth.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        monthPosition = position
                        Log.d("dateTest", "월position $monthPosition")
                        if(yearPosition != curYear || monthPosition != curMonth || dayPosition != curDay){
                            viewModel.setIsTodayToFalse()
                        }
                    }
                }
            })
            rvDay.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstVisibleItemPosition()
                        dayPosition = position
                        Log.d("dateTest", "일position $dayPosition")
                        if(yearPosition != curYear || monthPosition != curMonth || dayPosition != curDay){
                            viewModel.setIsTodayToFalse()
                        }
                    }
                }
            })
        }

    }

}