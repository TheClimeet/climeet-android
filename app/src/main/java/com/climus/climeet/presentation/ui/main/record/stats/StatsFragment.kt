package com.climus.climeet.presentation.ui.main.record.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentStatsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.stickchart.StickChartAdapter
import com.climus.climeet.presentation.ui.main.record.stats.datepicker.SelectYearMonthBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : BaseFragment<FragmentStatsBinding>(R.layout.fragment_stats) {
    private val viewModel: StatsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo StickChartAdapter 초기화만 여기서 시켜주기
        binding.rvStickChart.adapter = StickChartAdapter()
        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    StatsEvent.NavigateToSelectMonthYearBottomSheetFragment -> showBottomSheet()
                }
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


}