package com.climus.climeet.presentation.ui.main.record.stats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCalendarBinding
import com.climus.climeet.databinding.FragmentStatsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheet
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarEvent
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarViewModel
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth

@AndroidEntryPoint
class StatsFragment : BaseFragment<FragmentStatsBinding>(R.layout.fragment_stats) {
    private val viewModel: StatsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    else -> {}
                }
            }
        }
    }

}