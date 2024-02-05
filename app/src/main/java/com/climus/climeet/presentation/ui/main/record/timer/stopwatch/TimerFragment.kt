package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentTimerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.NoticePopup
import com.climus.climeet.presentation.ui.main.record.model.RecordCragData
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorImageAdapter
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordSectorLevelAdapter
import com.climus.climeet.presentation.ui.main.record.timer.adapter.RecordWallNameAdapter
import com.climus.climeet.presentation.ui.main.record.timer.data.ClimbingDatabase
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesDao
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesData
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesRepository
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesRepositoryImpl
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.SetTimerClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectBottomFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.CragSelectionListener
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ViewMode {
    START, PAUSE, RESTART, STOP
}

class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer),
    CragSelectionListener {

    private val timerVM: TimerViewModel by activityViewModels()
    private val recordVM: SetTimerClimbingRecordViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainVm = timerVM
        binding.recordVM = recordVM

        timerVM.registerReceiver(requireContext())
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // 서비스 강제 종료시 stop 상태로 reset
        val serviceState = TimerService.serviceRunning
        Log.d("timer", "서비스 실행중? : ${serviceState.value}")
        if (serviceState.value == null) {
            stopStopwatch()
            timerVM.isStart.value = false
            timerVM.isPaused.value = false
            timerVM.isRestart.value = false
            timerVM.isStop.value = true
            timerVM.isRunning.value = false
            timerVM.pauseTime.value = 0L
            Log.d("timer", "서비스 종료 상태라 값 초기화")
        } else {
            initViewModel()
        }

        initTimerLayout()
        initClickListener()
        timerObserve()
        pauseObserve()

        // 루트 기록
        setRecyclerView()
        setRecyclerViewTouchEvent()
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

    override fun onResume() {
        super.onResume()
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
        //Log.d("timer", "timer onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        timerVM.unregisterReceiver(requireContext())
        //Log.d("timer", "timer onDestroy")
    }

    private fun initViewModel() {
        timerVM.isStart.value = sharedPreferences.getBoolean(KEY_IS_START, false)
        timerVM.isPaused.value = sharedPreferences.getBoolean(KEY_IS_PAUSE, false)
        timerVM.isRestart.value = sharedPreferences.getBoolean(KEY_IS_RESTART, false)
        timerVM.isStop.value = sharedPreferences.getBoolean(KEY_IS_STOP, true)
        timerVM.isRunning.value = sharedPreferences.getBoolean(KEY_IS_RUNNING, true)
        timerVM.pauseTime.value = sharedPreferences.getLong(PAUSE_TIME, 0L)
        Log.d(
            "timer",
            "뷰 변수 초기화\nisStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isStart.value}, pauseTime = ${timerVM.pauseTime.value}"
        )
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
                "[값 업데이트] isStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isRunning.value}"
            )
        }
    }

    private fun timerObserve() {
        // pause 시간 계산
        timerVM.pauseTimeFormat.observe(viewLifecycleOwner, Observer { pauseTime ->
            if (pauseTime != null && pauseTime != "00:00") {
                sharedPreferences.edit().putString("pauseTimeFormat", pauseTime).apply()
                binding.tvTime.text = pauseTime
                timerVM.isPaused.value = true
                Log.d("timer", "[일시정지] timerObserve 일시정지 시간 업데이트 : $pauseTime")
            }
        })

        if (timerVM.isPaused.value == true) {
            // 재실행 시 pause 일 때 시간 보여주기
            val time = sharedPreferences.getString("pauseTimeFormat", "00:00")
            binding.tvTime.text = time
            Log.d("timer", "[일시정지] timerObserve 호출 : $time")
        } else {
            timerVM.timeFormat.observe(viewLifecycleOwner, Observer { timeFormat ->
                // 시간 흘러감
                binding.tvTime.text = timeFormat
                //Log.d("timer", "[진행중] timerObserve 호출 : $timeFormat")
            })
        }
    }

    private fun pauseObserve() {
        timerVM.pauseState.observe(viewLifecycleOwner, Observer { state ->
            if (state == "yes") {
                viewMode(ViewMode.PAUSE)
                timerVM.isRunning.value = false
            } else if (state == "no") {
                viewMode(ViewMode.RESTART)
                timerVM.isRunning.value = true
            }
        })
    }

    private fun initTimerLayout() {
        timerVM.isStart.observe(viewLifecycleOwner, Observer { isStart ->
            if (isStart) {
                viewMode(ViewMode.START)
                Log.d("timer", "화면 초기화 : start")
            }
        })
        timerVM.isPaused.observe(viewLifecycleOwner, Observer { isPaused ->
            if (isPaused) {
                viewMode(ViewMode.PAUSE)
//                val time = sharedPreferences.getString("pauseTimeFormat", "null값")
//                binding.tvTime.text = time
                Log.d("timer", "화면 초기화 : paused")
            }
        })
        timerVM.isRestart.observe(viewLifecycleOwner, Observer { isRestart ->
            if (isRestart) {
                viewMode(ViewMode.RESTART)
                timerObserve()
                Log.d("timer", "화면 초기화 : restart")
            }
        })
        timerVM.isStop.observe(viewLifecycleOwner, Observer { isStop ->
            if (isStop) {
                viewMode(ViewMode.STOP)
//                updateStatePref()
                Log.d("timer", "화면 초기화 : stop")
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
            }
        }
        // 스톱워치 일시정지
        binding.ivPause.setOnClickListener {
            pauseStopwatch()
        }
        // 스톱워치 재시작
        binding.ivRestart.setOnClickListener {
            restartStopwatch()
        }
        // 정지 버튼 눌림
        binding.ivStop.setOnClickListener {
            NoticePopup.make(it, "정지 버튼을 길게 누르면\n운동이 종료됩니다.").show()
        }
        binding.ivStop.setOnLongClickListener {
            stopStopwatch()

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
        timerVM.isRunning.value = true
        timerVM.isStart.value = true
        timerVM.isPaused.value = false
        timerVM.isRestart.value = false
        timerVM.isStop.value = false
        //Log.d("timer", "startStopwatch 호출")
    }

    private fun pauseStopwatch() {
        viewMode(ViewMode.PAUSE)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "PAUSE")
        }
        context?.startService(intent)
        timerVM.pauseTime.value = sharedPreferences.getLong("pauseTime", 0L)
        timerVM.isRunning.value = false
        timerVM.isStart.value = false
        timerVM.isPaused.value = true
        timerVM.isRestart.value = false
        timerVM.isStop.value = false
        Log.d("timer", "pauseStopwatch 호출")
    }

    private fun restartStopwatch() {
        viewMode(ViewMode.RESTART)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "RESTART")
        }
        context?.startService(intent)
        timerVM.isRunning.value = true
        timerVM.isPaused.value = false
        timerVM.isStart.value = false
        timerVM.isRestart.value = true
        timerVM.isStop.value = false
        //Log.d("timer", "restartStopwatch 호출")
    }

    private fun stopStopwatch() {
        viewMode(ViewMode.STOP)
        binding.tvTime.text = "00:00"
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "STOP")
        }
        context?.startService(intent)
        timerVM.isPaused.value = false
        timerVM.isStart.value = false
        timerVM.isRestart.value = false
        timerVM.isStop.value = true
        timerVM.isRunning.value = false
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
        const val PAUSE_TIME = "pauseTime"
        const val KEY_IS_START = "isStart"
        const val KEY_IS_PAUSE = "isPause"
        const val KEY_IS_RESTART = "isRestart"
        const val KEY_IS_STOP = "isStop"
        const val KEY_IS_RUNNING = "isRunning"
    }
}