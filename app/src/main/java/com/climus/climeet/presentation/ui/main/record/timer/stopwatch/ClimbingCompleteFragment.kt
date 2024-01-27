package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClimbingCompleteFragment :
    BaseFragment<FragmentTimerCompleteBinding>(R.layout.fragment_timer_complete) {

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        // 바텀 네비게이션 뷰 보이기
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bnv).visibility = View.VISIBLE

        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 바텀 네비게이션 뷰 숨기기
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bnv).visibility = View.GONE

        binding.ivCheck.setImageResource(R.drawable.ic_check_anim)
        val drawable = binding.ivCheck.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }

        handler.postDelayed(runnable, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}