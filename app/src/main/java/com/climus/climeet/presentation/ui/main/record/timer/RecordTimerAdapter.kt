package com.climus.climeet.presentation.ui.main.record.timer

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecordTimerAdapter(
    fragment: Fragment,
    private val onStartClickListener: TimerFragment.OnStartClickListener
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // 페이지 위치에 따라 다른 fragment 보여주기
        return when (position) {
            0 -> TimerFragment().also { it.onStartClickListener = onStartClickListener }
            else -> TimerExerciseFragment()
        }
    }
}