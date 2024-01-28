package com.climus.climeet.presentation.ui.main.record.timer

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.SetTimerClimbingRecordFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel

class TimerMainAdapter(
    fragment: Fragment,
    private val timerStartedProvider: TimerViewModel
) : FragmentStateAdapter(fragment) {

    private val isStart = timerStartedProvider.isTimerStarted()
    override fun getItemCount(): Int = if (isStart) 2 else 1

    override fun createFragment(position: Int): Fragment {
        // 페이지 위치에 따라 다른 fragment 보여주기
        return when (position) {
            0 -> TimerFragment()
            else -> {
                if (isStart) {
                    // 스톱워치가 시작되었다면 TimerExerciseFragment(루트 기록) 보여주기
                    Log.d("timer", "adapter 루트기록 연결o")
                    SetTimerClimbingRecordFragment()
                } else {
                    // 스톱워치가 시작되지 않았다면 아무 것도 생성하지 않음
                    Log.d("timer", "adapter 루트기록 연결x")
                    throw IllegalStateException("Invalid position $position")
                }
            }
        }
    }
}