package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentTimerRecordBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.NoticePopup
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.RouteImageAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.record.timer.TimerMainViewModel
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.TimerViewModel
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.TimerCragSelectBottomSheetViewModel

// ------------------------ 시간 흘러갈 때 스와이프 하면 보이는 루트기록 화면 -------------------------
class SetTimerClimbingRecordFragment :
    BaseFragment<FragmentTimerRecordBinding>(R.layout.fragment_timer_record) {

    private val viewModel: SetTimerClimbingRecordViewModel by activityViewModels()
    private val timerVM: TimerViewModel by activityViewModels()
    private val cragSelectVM: TimerCragSelectBottomSheetViewModel by activityViewModels()
    private val mainVM: TimerMainViewModel by activityViewModels()

    private var cragId: Long = 0L
    private var cragName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.ivCelebrate.bringToFront()

        setRecyclerView()
        timerObserve()
        initClickListener()
        initEventObserve()
        setCragName()
    }

    // 앱 종료 후 재시작 시에 루트기록 화면에 암장 이름 보여주기 위함
    override fun onResume() {
        super.onResume()
        val cragName =
            sharedPreferences.getString("cragName", getString(R.string.timer_crag_set_inform))
        binding.tvTitle.text = cragName
    }

    private fun setRecyclerView() {
        binding.rvSectorName.adapter = SectorNameAdapter()
        binding.rvSectorLevel.adapter = GymLevelAdapter()
        binding.rvSectorImage.adapter = RouteImageAdapter()
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
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

        // 시간 클릭시 스톱워치 화면으로 이동하기 위한 이벤트를 발생시킨다
        binding.layoutIdcTime.setOnClickListener {
            mainVM.moveToStopwatch()
            Log.d("move", "시간 눌림")
        }
    }

    // 루트기록 화면에 암장 이름 설정
    private fun setCragName() {
        cragSelectVM.selectedCrag.observe(viewLifecycleOwner, Observer { cragData ->
            if (cragData != null) {
                cragId = cragData.id
                cragName = cragData.name

                // 암장 이름 보여주기
                binding.tvTitle.text = cragName

                // 암장 이름 뷰모델에 저장
                viewModel.selectedCrag(cragId, cragName)
            }
        })
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is CreateRecordEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }
}