package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentTimerRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.idpw.SetAdminIdPwEvent
import com.climus.climeet.presentation.ui.main.record.timer.TimerMainFragment
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorImageAdapter
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorLevelAdapter
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordWallNameAdapter
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel

class SetTimerClimbingRecordFragment: BaseFragment<FragmentTimerRecordBinding>(R.layout.fragment_timer_record) {

    private val viewModel: SetTimerClimbingRecordViewModel by activityViewModels()
    private val timerVM: TimerViewModel by activityViewModels()

    private var cragId : Long = 0L
    private var cragName : String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        cragId = sharedPreferences.getLong("cragId", 1)
        cragName =
            sharedPreferences.getString("cragName", getString(R.string.timer_crag_set_inform)).toString()

        // 암장 이름 보여주기
        binding.tvTitle.text = cragName

        viewModel.setCragInfo(cragId, cragName)

        binding.ivCelebrate.bringToFront()

        setRecyclerView()
        setRecyclerViewTouchEvent()
        timerObserve()

        findNavController().currentDestination
    }

    private fun setRecyclerView() {
        binding.rvSectorName.adapter = RecordWallNameAdapter()
        binding.rvSectorLevel.adapter = RecordSectorLevelAdapter()
        binding.rvSectorImage.adapter = RecordSectorImageAdapter()
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
    }

    private fun setRecyclerViewTouchEvent(){
        binding.rvSectorImage.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

        binding.rvSectorName.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

        binding.rvSectorLevel.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })
    }

    // 시간 띄워줌
    private fun timerObserve() {
        timerVM.timeFormat.observe(viewLifecycleOwner, Observer { timeFormat ->
            binding.tvTime.text = timeFormat
        })
    }

    // todo : 시간 클릭시 스톱워치로 이동
}