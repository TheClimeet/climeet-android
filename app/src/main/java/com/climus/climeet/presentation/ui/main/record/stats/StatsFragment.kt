package com.climus.climeet.presentation.ui.main.record.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentStatsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.stats.datepicker.SelectYearMonthBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : BaseFragment<FragmentStatsBinding>(R.layout.fragment_stats) {
    private val viewModel: StatsViewModel by activityViewModels()
    private var adapter = SelectGymAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()
        initStateObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    StatsEvent.NavigateToSelectMonthYearBottomSheetFragment -> showBottomSheet()
                    StatsEvent.ShowPopupWindow -> showPopupWindow()
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                binding.viewStickchart.setupChartData(it.chartUiList)
                adapter.submitList(it.gymList)
            }
        }
    }

    private fun showBottomSheet(){
        viewModel.selectedDate.value?.let {
            SelectYearMonthBottomSheet(
                requireContext(),
                it
            ) { date ->
                viewModel.setSelectedDate(date)
            }.show()
        }
    }

    private fun showPopupWindow() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_select_gym, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_select_gym)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.submitList(viewModel.uiState.value.gymList)

        recyclerView.adapter = adapter

        val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.elevation = 10f

        // 외부 터치 시 팝업 닫기 설정
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        popupWindow.showAsDropDown(binding.layoutToggle, 0, 0)
    }


}