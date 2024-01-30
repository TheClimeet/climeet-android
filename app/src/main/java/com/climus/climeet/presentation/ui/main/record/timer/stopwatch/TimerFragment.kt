package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentTimerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.NoticePopup
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectBottomFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectionListener

enum class ViewMode {
    START, PAUSE, RESTART, STOP
}

class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer),
    CragSelectionListener {

    private val timerVM: TimerViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = timerVM
        timerVM.registerReceiver(requireContext())
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        initTimerLayout()
        initClickListener()
        timerObserve()
        Log.d("timer", "onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        timerVM.isStart.value = sharedPreferences.getBoolean(KEY_IS_START, false)
        timerVM.isPaused.value = sharedPreferences.getBoolean(KEY_IS_PAUSE, false)
        timerVM.isRestart.value = sharedPreferences.getBoolean(KEY_IS_RESTART, false)
        timerVM.isStop.value = sharedPreferences.getBoolean(KEY_IS_STOP, true)
        timerVM.isRunning.value = sharedPreferences.getBoolean(KEY_IS_RUNNING, true)
        // 암장 이름 설정
        if (timerVM.isStop.value == true) {
            binding.tvTitle.text = getString(R.string.timer_crag_set_inform)
        } else {
            binding.tvTitle.text =
                sharedPreferences.getString("cragName", getString(R.string.timer_crag_set_inform))
        }
        timerObserve()
        Log.d(
            "timer",
            "timer onResume\nisStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isStart.value}"
        )
    }

    override fun onPause() {
        super.onPause()
        updateStatePref()
        Log.d("timer", "timer onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        timerVM.unregisterReceiver(requireContext())
        Log.d("timer", "timer onDestroy")
    }

    private fun updateStatePref() {
        with(sharedPreferences.edit()) {
            timerVM.isStart.value?.let { putBoolean(KEY_IS_START, it) }
            timerVM.isPaused.value?.let { putBoolean(KEY_IS_PAUSE, it) }
            timerVM.isRestart.value?.let { putBoolean(KEY_IS_RESTART, it) }
            timerVM.isStop.value?.let { putBoolean(KEY_IS_STOP, it) }
            timerVM.isRunning.value?.let { putBoolean(KEY_IS_RUNNING, it) }
            apply()
            Log.d(
                "timer",
                "[중간 저장] isStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isStart.value}"
            )
        }
    }

    private fun timerObserve() {
        if (timerVM.isPaused.value == true) {
            val time = sharedPreferences.getString("pauseTime", timerVM.timeFormat.value)
            binding.tvTime.text = time
            //Log.d("timer", "timerObserve 호출 : $time")
        } else {
            timerVM.timeFormat.observe(viewLifecycleOwner, Observer { timeFormat ->
                binding.tvTime.text = timeFormat
            })
        }
    }

    private fun initTimerLayout() {
        timerVM.isStart.observe(viewLifecycleOwner, Observer { isStart ->
            if (isStart) {
                viewMode(ViewMode.START)
                //Log.d("timer", "화면 초기화 : start")
            }
        })
        timerVM.isPaused.observe(viewLifecycleOwner, Observer { isPaused ->
            if (isPaused) {
                viewMode(ViewMode.PAUSE)
                //Log.d("timer", "화면 초기화 : paused")
            }
        })
        timerVM.isRestart.observe(viewLifecycleOwner, Observer { isRestart ->
            if (isRestart) {
                viewMode(ViewMode.RESTART)
                timerObserve()
                //Log.d("timer", "화면 초기화 : restart")
            }
        })
        timerVM.isStop.observe(viewLifecycleOwner, Observer { isStop ->
            if (isStop) {
                viewMode(ViewMode.STOP)
                //Log.d("timer", "화면 초기화 : stop")
            }
        })
    }

    private fun initClickListener() {
        // 암장 선택
        binding.layoutSelectCrag.setOnClickListener {
            if (timerVM.isStop.value == false) {
                NoticePopup.make(it, "운동중에는 암장을 바꿀 수 없어요!").show()
            } else {
                showBottomSheet()
            }
        }

        // 스톱워치 시작
        binding.ivStart.setOnClickListener {
            if (binding.tvTitle.text == getString(R.string.timer_crag_set_inform)) {
                NoticePopup.make(it, "운동 전, 암장을 먼저 선택해주세요.").show()
            } else {
                startStopwatch()    // 스톱워치 서비스 시작
                timerVM.isStart.value = true
                timerVM.isPaused.value = false
                timerVM.isRestart.value = false
                timerVM.isStop.value = false
                timerVM.isRunning.value = true
                updateStatePref()
            }
        }

        // 스톱워치 일시정지
        binding.ivPause.setOnClickListener {
            pauseStopwatch()
            timerVM.isStart.value = false
            timerVM.isPaused.value = true
            timerVM.isRestart.value = false
            timerVM.isStop.value = false
            timerVM.isRunning.value = false
            updateStatePref()
            sharedPreferences.edit().putString("pauseTime", timerVM.timeFormat.value).apply()
        }

        // 스톱워치 재시작
        binding.ivRestart.setOnClickListener {
            restartStopwatch()
            timerVM.isStart.value = false
            timerVM.isPaused.value = false
            timerVM.isRestart.value = true
            timerVM.isStop.value = false
            timerVM.isRunning.value = true
            updateStatePref()
        }

        // 정지 버튼 눌림
        binding.ivStop.setOnClickListener {
            NoticePopup.make(it, "정지 버튼을 길게 누르면\n운동이 종료됩니다.").show()
        }

        binding.ivStop.setOnLongClickListener {
            stopStopwatch()
            timerVM.isPaused.value = false
            timerVM.isStart.value = false
            timerVM.isRestart.value = false
            timerVM.isStop.value = true
            timerVM.isRunning.value = false
            updateStatePref()

            // 완료화면 띄우기
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.main_container, ClimbingCompleteFragment())
            transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있게 스택에 추가
            transaction.commit()
            true    // OnClickListener와 같이 쓰기 위해 true로 설정
        }
    }

    private fun startStopwatch() {
        viewMode(ViewMode.START)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "START")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(intent)
        } else {
            context?.startService(intent)
        }
        //Log.d("timer", "startStopwatch 호출")
    }

    private fun pauseStopwatch() {
        viewMode(ViewMode.PAUSE)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "PAUSE")
        }
        context?.startService(intent)
        //Log.d("timer", "pauseStopwatch 호출")
    }

    private fun restartStopwatch() {
        viewMode(ViewMode.RESTART)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "RESTART")
        }
        context?.startService(intent)
        //Log.d("timer", "restartStopwatch 호출")
    }

    private fun stopStopwatch() {
        viewMode(ViewMode.STOP)
        binding.tvTime.text = "00:00"
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "STOP")
        }
        context?.startService(intent)
        //Log.d("stopStopwatch", "stopStopwatch 호출")
    }

    override fun onCragSelected(crag: RecordCragData) {
        // 선택된 암장의 이름 보여주기
        binding.tvTitle.text = crag.name
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
                binding.ivPause.visibility = View.VISIBLE
                binding.ivRestart.visibility = View.GONE
                binding.ivStop.visibility = View.VISIBLE
            }

            ViewMode.PAUSE -> {
                // [gone: 일시정지 버튼] [현재 visible: 정지, 재시작 버튼]
                binding.ivStart.visibility = View.GONE
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.VISIBLE
                binding.ivStop.visibility = View.VISIBLE
            }

            ViewMode.RESTART -> {
                // [gone: 재시작 버튼] [현재 visible: 정지, 일시정지 버튼]
                binding.ivStart.visibility = View.GONE
                binding.ivPause.visibility = View.VISIBLE
                binding.ivRestart.visibility = View.GONE
                binding.ivStop.visibility = View.VISIBLE
            }

            ViewMode.STOP -> {
                //[gone: 정지, 재시작, 일시정지 버튼] [현재 visible: 시작 버튼]
                binding.ivStart.visibility = View.VISIBLE
                binding.ivPause.visibility = View.GONE
                binding.ivRestart.visibility = View.GONE
                binding.ivStop.visibility = View.GONE
            }
        }
    }

    companion object {
        const val PREF_NAME = "timer_prefs"
        const val KEY_IS_START = "isStart"
        const val KEY_IS_PAUSE = "isPause"
        const val KEY_IS_RESTART = "isRestart"
        const val KEY_IS_STOP = "isStop"
        const val KEY_IS_RUNNING = "isRunning"
    }
}