package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentTimerRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.NoticePopup
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorImageAdapter
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorLevelAdapter
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorNameAdapter
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel

// ------------------------ 시간 흘러갈 때 스와이프 하면 보이는 루트기록 화면 -------------------------
class SetTimerClimbingRecordFragment :
    BaseFragment<FragmentTimerRecordBinding>(R.layout.fragment_timer_record) {

    private val viewModel: SetTimerClimbingRecordViewModel by activityViewModels()
    private val timerVM: TimerViewModel by activityViewModels()

    private var cragId: Long = 0L
    private var cragName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        cragId = sharedPreferences.getLong("cragId", 1)
        cragName =
            sharedPreferences.getString("cragName", getString(R.string.timer_crag_set_inform)).toString()

        // 암장 이름 보여주기
        binding.tvTitle.text = cragName

        // 암장 이름 설정
        viewModel.selectCrag(cragId, cragName)

        binding.ivCelebrate.bringToFront()

        setRecyclerView()
        setRecyclerViewTouchEvent()
        timerObserve()
        initClickListener()

        findNavController().currentDestination
    }

    private fun setRecyclerView() {
        binding.rvSectorName.adapter = RecordSectorNameAdapter()
        binding.rvSectorLevel.adapter = RecordSectorLevelAdapter()
        binding.rvSectorImage.adapter = RecordSectorImageAdapter()
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
    }

    private fun setRecyclerViewTouchEvent() {
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

    // 아래 main color로 된 부분에 시간을 보여준다
    private fun timerObserve() {
        timerVM.timeFormat.observe(viewLifecycleOwner, Observer { timeFormat ->
            binding.tvTime.text = timeFormat
        })
    }

    private fun initClickListener() {
        // 암장 이름이 보여지는 영역을 누르면 팝업 메세지를 보여준다
        binding.layoutSelectCrag.setOnClickListener {
            if (timerVM.isStop.value == false) {
                NoticePopup.make(it, "운동중에는 암장을 바꿀 수 없어요!").show()
            }
        }
    }

    // todo : 아래 시간 클릭 시 스톱워치 화면으로 이동
}