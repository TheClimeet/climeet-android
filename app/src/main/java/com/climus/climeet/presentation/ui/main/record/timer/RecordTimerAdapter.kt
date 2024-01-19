package com.climus.climeet.presentation.ui.main.record.timer

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.record.timer.exercise.TimerExerciseFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerFragment

class RecordTimerAdapter(
    fragment: Fragment,
    private val onStartClickListener: TimerFragment.OnStartClickListener
) : FragmentStateAdapter(fragment) {

    private var timerStarted = false
    fun setTimerStarted(started: Boolean) {
        timerStarted = started
        notifyDataSetChanged()
        Log.d("timer", "timerStarted : ${timerStarted}")
    }

    override fun getItemCount(): Int = if (timerStarted) 2 else 1

    override fun createFragment(position: Int): Fragment {
        // 페이지 위치에 따라 다른 fragment 보여주기
        return when (position) {
            0 -> TimerFragment().also { it.onStartClickListener = onStartClickListener }
            else -> {
                if (timerStarted) {
                    // 스톱워치가 시작되었다면 TimerExerciseFragment(루트 기록) 보여주기
                    TimerExerciseFragment()
                } else {
                    // 스톱워치가 시작되지 않았다면 아무 것도 생성하지 않음
                    throw IllegalStateException("Invalid position $position")
                }
            }
        }
    }
}