package com.climus.climeet.presentation.ui.main.record.timer.stopwatch

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentTimerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.NoticePopup
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.RouteImageAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.record.model.LevelItemForAvg
import com.climus.climeet.data.local.ClimbingRecordData
import com.climus.climeet.data.local.RouteRecordData
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.ClimbingRecordAdapter
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.CreateRecordUiState
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.RecordAverageAdapter
import com.climus.climeet.presentation.ui.main.record.timer.setrecord.SetTimerClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.TimerCragSelectBottomSheetFragment
import com.climus.climeet.presentation.ui.main.record.timer.stopwatch.selectcrag.TimerCragSelectBottomSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

// ------------------------ 스톱워치 화면 -------------------------
enum class ViewMode {
    START, PAUSE, RESTART, STOP
}

@AndroidEntryPoint
class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer) {

    // room DB
    @Inject
    lateinit var repository: MainRepository

    private val timerVM: TimerViewModel by activityViewModels()
    private val recordVM: SetTimerClimbingRecordViewModel by activityViewModels()   // 일시정지하면 보이는 루트기록을 위해 연결
    private val cragSelectVM: TimerCragSelectBottomSheetViewModel by activityViewModels()

    private lateinit var routeItemAdapter: ClimbingRecordAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainVM = timerVM
        binding.recordVM = recordVM

        routeItemAdapter = ClimbingRecordAdapter(recordVM)

        timerVM.registerReceiver(requireContext())
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        checkService()
        initTimerLayout()
        initClickListener()
        timerObserve()
        pauseObserve()
        initRouteObserve()

        // 암장 이름 설정
        initCragName()
        // 평균 완등률 설정
        setAverage()
        // 레벨 별 완등률
        setAverageByLevel()
        // 루트 기록
        setRouteRecyclerView()

    }

    private fun checkService() {
        // 강제 종료시 stop 상태로 초기화 하기 위해 TimerService에서 serviceRunning 변수 사용
        // true : 스톱워치 서비스 실행중 (강제 종료 되지 않은 상태)
        // null : 스톱워치 동작 안 함 (강제 종료 or 처음 실행하는 상태)
        val serviceState = TimerService.serviceRunning

        Log.d("TIMER", "서비스 실행중? : ${serviceState.value}")

        if (serviceState.value == null) {
            stopStopwatch()
            timerVM.isStart.value = false
            timerVM.isPaused.value = false
            timerVM.isRestart.value = false
            timerVM.isStop.value = true
            timerVM.isRunning.value = false
            timerVM.pauseTime.value = 0L
            timerVM.pauseState.value = ""
            updateStatePref()

            clearRouteRecord()

        } else {
            // 스톱워치 서비스가 실행중인 상태 -> spf값으로 뷰모델 변수 초기화 -> initTimerLayout() 통해 ViewMode 설정
            setViewModel()
        }
    }

    private fun clearRouteRecord() {
        // 저장된 루트기록 초기화
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllRoute()
            // 잘 지워졌나 확인
            val result = repository.getAllRoute()
            delay(1000)
            Log.d("recorddd", "저장된 기록들 reset $result")
        }
    }

    private fun initCragName() {
        cragSelectVM.selectedCrag.observe(viewLifecycleOwner, Observer { cragData ->
            if (cragData != null) {
                val cragId = cragData.id
                val cragName = cragData.name
                val date = LocalDate.now().toString()
                sharedPreferences.edit().putString("cragName", cragName).apply()
                sharedPreferences.edit().putLong("cragId", cragId).apply()

                // 선택된 암장의 이름 보여주기
                binding.tvTitle.text = cragName
                //Log.d("TIMER", "timerfragment 이름 설정함 id :$cragId, name : $cragName, data : $date")

                // room db에 암장정보 저장
                val recordData = ClimbingRecordData(
                    id = 1,
                    gymId = cragId,
                    gymName = cragName,
                    date = date,
                    time = "",
                    avgDifficulty = 0
                )

                CoroutineScope(Dispatchers.IO).launch {
                    // 저장
                    repository.insert(recordData)
                    // 저장된 값 확인
                    val result = repository.getAllRecord()
                    delay(1000)
                    Log.d("recorddd", result.toString())
                }
            }
        })
    }

    // 평균 완등률 설정
    private fun setAverage() {
        recordVM.avgCompleteRate.observe(viewLifecycleOwner, Observer { rate ->
            val progress = (rate * 100).roundToInt()
            binding.pbAvgComplete.progress = progress
            binding.tvAvgComplete.text = "$progress%"
        })
    }

    // 도전한 레벨 별 평균 완등률
    private fun setAverageByLevel() {
        recordVM.updateRecord.observe(viewLifecycleOwner, Observer {
            var items: List<LevelItemForAvg>
            val levelAdapter = RecordAverageAdapter(recordVM)
            binding.rvAvgRecord.adapter = levelAdapter

            CoroutineScope(Dispatchers.IO).launch {
                val levelData = repository.getAllLevelRecord()
                val firstRecords = mutableListOf<RouteRecordData>() // 중복 없게 각 레벨별 첫번째 데이터만 골라냄

                val levelNameSet = mutableSetOf<String>()
                for (record in levelData) {
                    if (!levelNameSet.contains(record.levelName)) {
                        levelNameSet.add(record.levelName)
                        firstRecords.add(record)
                    }
                }
                if (firstRecords != null) {
                    items = firstRecords.map { data ->
                        val attemptCount = repository.getAttemptCount(data.levelName)
                        val successCount = repository.getSuccessCount(data.levelName)
                        val levelColor = data.levelColor
                        LevelItemForAvg(data.levelName, levelColor, attemptCount, successCount)
                    }
                    withContext(Dispatchers.Main) {
                        levelAdapter.setItems(items)
                    }
                }
            }
        })
    }

    private fun setRouteRecyclerView() {
        binding.rvSectorName.adapter = SectorNameAdapter()
        binding.rvSectorLevel.adapter = GymLevelAdapter()
        binding.rvSectorImage.adapter = RouteImageAdapter()
        binding.rvRouteRecord.adapter = routeItemAdapter
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
    }

    // 루트기록 더보기 item
    private fun initRouteObserve() {
        repeatOnStarted {
            recordVM.items.collect { items ->
                routeItemAdapter.items = items
                routeItemAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceState = TimerService.serviceRunning

        if (serviceState.value != null) {
            val id = sharedPreferences.getLong("cragId", 0)
            val name = sharedPreferences.getString("cragName", getString(R.string.timer_crag_set_inform))
            if (name != null) {
                // 앱 재시작 시, 암장 정보 가져오는 API 재호출
                recordVM.selectedCrag(id, name)
                recordVM.isRestart.value = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 암장 이름 및 상단 요소 설정
        if (timerVM.isStop.value == true) {
            binding.tvTitle.text = getString(R.string.timer_crag_set_inform)
            recordVM.totalRoute.value = "--"
            recordVM.totalComplete.value = "--"
            recordVM.avgLevel.value = "--"
        } else {
            binding.tvTitle.text =
                sharedPreferences.getString("cragName", getString(R.string.timer_crag_set_inform))
        }

        // spf에 저장된 값 불러와 뷰모델에 저장
        setViewModel()
        timerObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerVM.unregisterReceiver(requireContext())
        //Log.d("TIMER", "timer onDestroy")
    }

    // spf -> viewmodel
    // spf에 저장된 값으로 뷰모델 값을 변경한다
    // 값이 변경되면 각 값을 observe하고 있는 initTimerLayout()에서 ViewMode를 변경해준다
    private fun setViewModel() {
        timerVM.isStart.value = sharedPreferences.getBoolean(KEY_IS_START, false)
        timerVM.isPaused.value = sharedPreferences.getBoolean(KEY_IS_PAUSE, false)
        timerVM.isRestart.value = sharedPreferences.getBoolean(KEY_IS_RESTART, false)
        timerVM.isStop.value = sharedPreferences.getBoolean(KEY_IS_STOP, true)
        timerVM.isRunning.value = sharedPreferences.getBoolean(KEY_IS_RUNNING, true)
        timerVM.pauseTime.value = sharedPreferences.getLong(PAUSE_TIME, 0L)
//        Log.d(
//            "TIMER",
//            "TimerFragment [setViewModel] spf값 반영 \nisStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isStart.value}, pauseTime = ${timerVM.pauseTime.value}"
//        )
    }

    // viewmodel -> spf
    // 현재 뷰모델 변수 값을 spf에 저장해준다
    private fun updateStatePref() {
        with(sharedPreferences.edit()) {
            timerVM.isStart.value?.let { putBoolean(KEY_IS_START, it) }
            timerVM.isPaused.value?.let { putBoolean(KEY_IS_PAUSE, it) }
            timerVM.isRestart.value?.let { putBoolean(KEY_IS_RESTART, it) }
            timerVM.isStop.value?.let { putBoolean(KEY_IS_STOP, it) }
            timerVM.isRunning.value?.let { putBoolean(KEY_IS_RUNNING, it) }
            apply()
//            Log.d(
//                "TIMER",
//                "TimerFragment [updateStatePref] isStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isRunning.value}"
//            )
        }
    }

    private fun timerObserve() {
        // pauseTimeFormat
        // broadcast로 viewmodel에 넘어온 일시정지 시점의 시간을 "00:00"의 형식으로 바꾼 시간이다
        // pauseTimeFormat에 저장된 시간을 spf에 업데이트 해준다 (TimerViewModel에서는 spf에 저장해 줄 방법을 모르겠어서 이렇게 설정해 둔 상태)
        timerVM.pauseTimeFormat.observe(viewLifecycleOwner, Observer { pauseTime ->
            if (pauseTime != null && pauseTime != "00:00") {
                sharedPreferences.edit().putString("pauseTimeFormat", pauseTime).apply()
                binding.tvTime.text = pauseTime
                //Log.d("TIMER", "[일시정지] timerObserve 일시정지 시간 업데이트 : $pauseTime")
            }
        })

        if (timerVM.isPaused.value == true) {
            // 재실행 시, 스톱워치가 일시정지 상태일 때 시간 보여줌
            val time = sharedPreferences.getString("pauseTimeFormat", "00:00")
            binding.tvTime.text = time
            //Log.d("timer", "[일시정지] timerObserve 호출 : $time")
        } else {
            timerVM.timeFormat.observe(viewLifecycleOwner, Observer { timeFormat ->
                // 스톱워치 isStart, reStart일 때의 시간을 화면에 보여줌
                binding.tvTime.text = timeFormat
                //Log.d("TIMER", "[진행중] timerObserve 호출 : $timeFormat")
            })
        }
    }

    private fun pauseObserve() {
        timerVM.pauseState.observe(viewLifecycleOwner, Observer { state ->
            if (state == "yes") {
                viewMode(ViewMode.PAUSE)
                updateViewModel(
                    start = false,
                    pause = true,
                    restart = false,
                    stop = false,
                    running = false
                )
            } else if (state == "no") {
                viewMode(ViewMode.RESTART)
                updateViewModel(
                    start = false,
                    pause = false,
                    restart = true,
                    stop = false,
                    running = true
                )
            }
        })

        recordVM.resetView.observe(viewLifecycleOwner, Observer { reset ->
            if (reset){
                if (timerVM.isPaused.value == true){
                    // 일시정지 상태면 화면 재설정
                    binding.layoutAvgComplete.visibility = View.VISIBLE
                    binding.tvTimeTitle.visibility = View.VISIBLE
                    binding.layoutRouteRecord.visibility = View.VISIBLE
                    recordVM.isAvgToggleOn.value = false
                    recordVM.isRouteToggleOn.value = true
                }
            }
        })
    }

    // 화면 초기화 함수
    // TimerViewModel의 스톱워치 상태값을 관찰해 그애 맞는 화면 상태를 viewMode를 통해 보여준다
    private fun initTimerLayout() {
        timerVM.isStart.observe(viewLifecycleOwner, Observer { isStart ->
            if (isStart) {
                viewMode(ViewMode.START)
                //Log.d("TIMER", "화면 초기화 : start")
            }
        })
        timerVM.isPaused.observe(viewLifecycleOwner, Observer { isPaused ->
            if (isPaused) {
                viewMode(ViewMode.PAUSE)
                // 기록 유무에 따른 보이기 설정
                if (recordVM.itemsLiveData.value?.isEmpty() == false) {
                    binding.layoutAvgComplete.visibility = View.VISIBLE
                    binding.tvTimeTitle.visibility = View.VISIBLE
                    binding.layoutRouteRecord.visibility = View.VISIBLE
                    recordVM.isAvgToggleOn.value = false
                    recordVM.isRouteToggleOn.value = true
                }
                //Log.d("TIMER", "화면 초기화 : paused")
            }
        })
        timerVM.isRestart.observe(viewLifecycleOwner, Observer { isRestart ->
            if (isRestart) {
                viewMode(ViewMode.RESTART)
                timerObserve()

                binding.layoutAvgComplete.visibility = View.GONE
                binding.tvTimeTitle.visibility = View.GONE
                binding.layoutRouteRecord.visibility = View.GONE
                //Log.d("TIMER", "화면 초기화 : restart")
            }
        })
        timerVM.isStop.observe(viewLifecycleOwner, Observer { isStop ->
            if (isStop) {
                viewMode(ViewMode.STOP)

                binding.layoutAvgComplete.visibility = View.GONE
                binding.tvTimeTitle.visibility = View.GONE
                binding.layoutRouteRecord.visibility = View.GONE
                //Log.d("TIMER", "화면 초기화 : stop")
            }
        })
    }

    // 스톱워치 화면 내의 버튼 (재생, 정지, 일시정지, 재시작)을 눌렀을 때 설정
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
            // 종료 시간 저장
            CoroutineScope(Dispatchers.IO).launch {
                if(timerVM.isPaused.value == true){
                    timerVM.pauseTime.value?.let { pauseTime ->
                        sharedPreferences.edit().putLong("stopTime", pauseTime).commit()
                    }
                } else {
                    timerVM.time.value?.let { time ->
                        sharedPreferences.edit().putLong("stopTime", time).commit()
                    }
                }
            }
            stopStopwatch()

            // TimerCragSelectBottomSheetViewModel의 selectedCrag 초기화
            cragSelectVM.resetItem()

            // 루트기록 API로 전송
            timerVM.sendClimbingRecord()

            recordVM.resetAtStop()

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

        // todo : 서비스가 실행된 뒤 setViewModel함수를 호출해 spf값을 반영해주는게 가능하면 아래처럼 일일이 변수 업데이트 할 필요 없음
        updateViewModel(start = true, pause = false, restart = false, stop = false, running = true)
        //Log.d("TIMER", "startStopwatch 호출")
    }

    private fun pauseStopwatch() {
        viewMode(ViewMode.PAUSE)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "PAUSE")
        }
        context?.startService(intent)

        timerVM.pauseTime.value = sharedPreferences.getLong("pauseTime", 0L)

        // todo : 서비스가 실행된 뒤 setViewModel함수를 호출해 spf값을 반영해주는게 가능하면 아래처럼 일일이 변수 업데이트 할 필요 없음
        updateViewModel(start = false, pause = true, restart = false, stop = false, running = false)

        //Log.d("TIMER", "pauseStopwatch 호출")
    }

    private fun restartStopwatch() {
        viewMode(ViewMode.RESTART)
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "RESTART")
        }
        context?.startService(intent)

        // todo : 서비스가 실행된 뒤 setViewModel함수를 호출해 spf값을 반영해주는게 가능하면 아래처럼 일일이 변수 업데이트 할 필요 없음
        updateViewModel(start = false, pause = false, restart = true, stop = false, running = true)

        //Log.d("timer", "restartStopwatch 호출")
    }

    private fun stopStopwatch() {
        viewMode(ViewMode.STOP)
        binding.tvTime.text = "00:00"
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("command", "STOP")
        }
        context?.startService(intent)

        // todo : 서비스가 실행된 뒤 setViewModel함수를 호출해 spf값을 반영해주는게 가능하면 아래처럼 일일이 변수 업데이트 할 필요 없음
        updateViewModel(start = false, pause = false, restart = false, stop = true, running = false)

        sharedPreferences.edit().putString(TOP_CHALLENGE, "--").apply()
        sharedPreferences.edit().putString(TOP_COMPLETE, "--").apply()
        sharedPreferences.edit().putString(TOP_LEVEL, "--").apply()
        binding.tvTitle.text = getString(R.string.timer_crag_set_inform)

        // 루트기록 관련 초기화
        recordVM.totalRoute.value = "--"
        recordVM.totalComplete.value = "--"
        recordVM.avgLevel.value = "--"

        recordVM.apiCheck = false
        recordVM.isSelectedCrag.value = false

        //Log.d("stopStopwatch", "stopStopwatch 호출")
    }

    // 암장 선택 바텀시트를 보여준다
    private fun showBottomSheet() {
        cragSelectVM.resetItem()
        recordVM.apiCheck = false
        val bottomSheetFragment = TimerCragSelectBottomSheetFragment()
        bottomSheetFragment.show(parentFragmentManager, "timerCragSelectBottomSheet")
    }

    // TimerViewModel의 값을 스톱워치 상태에 맞게 업데이트 해준다
    private fun updateViewModel(
        start: Boolean,
        pause: Boolean,
        restart: Boolean,
        stop: Boolean,
        running: Boolean
    ) {
        timerVM.isStart.value = start
        timerVM.isPaused.value = pause
        timerVM.isRestart.value = restart
        timerVM.isStop.value = stop
        timerVM.isRunning.value = running
//        Log.d(
//            "TIMER",
//            "TimerFragment [버튼 눌림] isStart : ${timerVM.isStart.value}, isPause : ${timerVM.isPaused.value}, isRestart : ${timerVM.isRestart.value}, isStop : ${timerVM.isStop.value}, isRunning : ${timerVM.isRunning.value}"
//        )
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
        const val TOP_CHALLENGE = "timerChallenge"
        const val TOP_COMPLETE = "timerComplete"
        const val TOP_LEVEL = "timerLevel"
    }
}