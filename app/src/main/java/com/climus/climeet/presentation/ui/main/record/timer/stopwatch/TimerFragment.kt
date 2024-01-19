package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentRecordTimerBinding
import com.climus.climeet.presentation.base.BaseFragment

class TimerFragment: BaseFragment<FragmentRecordTimerBinding>(R.layout.fragment_record_timer) {

    private val viewModel: TimerViewModel by viewModels()

    interface OnStartClickListener {
        fun onStartClick()
    }
    var onStartClickListener: OnStartClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        // 시작 버튼 눌림
        binding.ivStart.setOnClickListener {
            onStartClickListener?.onStartClick()    // Indicator 보이기 설정

            viewMode("start")
        }
        // 일시정지 버튼 눌림
        binding.ivPause.setOnClickListener {
            viewMode("pause")
        }
        // 재시작 버튼 눌림
        binding.ivRestart.setOnClickListener {
            viewMode("restart")
        }
        // 정지 버튼 눌림
        binding.ivStop.setOnClickListener {
            // todo : 다이얼로그 띄우기
            Toast.makeText(requireContext(), "길게 누르자", Toast.LENGTH_SHORT).show()
        }
        binding.ivStop.setOnLongClickListener {
            viewMode("stop")
            onStartClickListener?.onStartClick()    // Indicator 보이기 설정
            true    // OnClickListener와 같이 쓰기 위해 true로 설정
        }
    }

    private fun viewMode (mode : String){
        when (mode) {
            "start" -> {
                // [gone: 시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivStart.visibility = View.GONE
                binding.ivStop.visibility = View.VISIBLE
                binding.ivPause.visibility = View.VISIBLE
            }
            "pause" -> {
                // [gone: 일시정지 버튼] [현재 visible: 정지, 재시작 버튼]
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.VISIBLE
            }
            "restart" -> {
                // [gone: 재시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivRestart.visibility = View.GONE
                binding.ivPause.visibility = View.VISIBLE

            }
            else -> { // "stop"
                //[gone: 정지, 재시작, 일시정지 버튼] [현재 visible: 시작 버튼]
                binding.ivStop.visibility = View.GONE
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.GONE
                binding.ivStart.visibility = View.VISIBLE
            }
        }
    }
}