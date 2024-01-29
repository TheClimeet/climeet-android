package com.climus.climeet.presentation.ui.main.record.timer

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.SetTimerClimbingRecordFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel

class TimerMainAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TimerFragment()
            else -> SetTimerClimbingRecordFragment()
        }
    }
}