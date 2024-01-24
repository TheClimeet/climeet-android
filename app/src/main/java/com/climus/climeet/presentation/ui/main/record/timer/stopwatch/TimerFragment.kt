package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectBottomFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectionListener

enum class ViewMode {
    START, PAUSE, RESTART, STOP
}

class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer),
    CragSelectionListener {

    private val timerVM: TimerViewModel by viewModels()

    interface OnStartClickListener {
        fun onStartClick()
    }

    var onStartClickListener: OnStartClickListener? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = timerVM

        // 암장 선택
        binding.layoutSelectCrag.setOnClickListener {
            showBottomSheet()
        }

        // 스톱워치 시작
        binding.ivStart.setOnClickListener {
            onStartClickListener?.onStartClick()    // Indicator 보이기 설정

            viewMode(ViewMode.START)
        }
        // 스톱워치 일시정지
        binding.ivPause.setOnClickListener {
            viewMode(ViewMode.PAUSE)
        }
        // 스톱워치 재시작
        binding.ivRestart.setOnClickListener {
            viewMode(ViewMode.RESTART)
        }
        // 정지 버튼 눌림
        binding.ivStop.setOnClickListener {
            // todo : 다이얼로그 띄우기
            Toast.makeText(requireContext(), "길게 누르자", Toast.LENGTH_SHORT).show()
        }
        binding.ivStop.setOnLongClickListener {
            viewMode(ViewMode.STOP)
            onStartClickListener?.onStartClick()    // Indicator 보이기 설정
            true    // OnClickListener와 같이 쓰기 위해 true로 설정
        }
    }

    override fun onCragSelected(crag: RecordCragData) {
        // 선택된 암장의 이름 보여주기
        binding.tvTitle.text = crag.name
    }

    private fun setCragSelected(selectedCrag: RecordCragData) {
        // 선택된 암장의 이름 보여주기
        binding.tvTitle.text = selectedCrag.name
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = CragSelectBottomFragment()
        bottomSheetFragment.cragSelectionListener = this
        bottomSheetFragment.show(parentFragmentManager, "timerCragSelectBottomSheet")
    }

    private fun viewMode(mode: ViewMode) {
        when (mode) {
            ViewMode.START -> {
                // [gone: 시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivStart.visibility = View.GONE
                binding.ivStop.visibility = View.VISIBLE
                binding.ivPause.visibility = View.VISIBLE
            }

            ViewMode.PAUSE -> {
                // [gone: 일시정지 버튼] [현재 visible: 정지, 재시작 버튼]
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.VISIBLE
            }

            ViewMode.RESTART -> {
                // [gone: 재시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivRestart.visibility = View.GONE
                binding.ivPause.visibility = View.VISIBLE

            }

            ViewMode.STOP -> {
                //[gone: 정지, 재시작, 일시정지 버튼] [현재 visible: 시작 버튼]
                binding.ivStop.visibility = View.GONE
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.GONE
                binding.ivStart.visibility = View.VISIBLE
            }
        }
    }
}