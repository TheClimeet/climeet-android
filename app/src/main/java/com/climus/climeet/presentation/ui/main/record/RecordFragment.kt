package com.climus.climeet.presentation.ui.main.record

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.calendar.CalendarFragment

class RecordFragment : BaseFragment<FragmentRecordBinding>(R.layout.fragment_record) {
    private val viewModel: RecordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        showFragment(CalendarFragment())
        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect {
                when(it){
                    RecordEvent.ShowCalendarFragment -> showCalendar()
                    RecordEvent.ShowStatsFragment -> showStats()
                }
            }
        }
    }

    private fun showCalendar(){
        binding.tvRecord.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.tvStat.setTextColor(ContextCompat.getColor(requireContext(), R.color.cm_silver))
        showFragment(CalendarFragment())
    }

    private fun showStats(){
        binding.tvRecord.setTextColor(ContextCompat.getColor(requireContext(), R.color.cm_silver))
        binding.tvStat.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        showFragment(CalendarFragment())
    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container_child_fragment, fragment)
            .commit()
    }

}