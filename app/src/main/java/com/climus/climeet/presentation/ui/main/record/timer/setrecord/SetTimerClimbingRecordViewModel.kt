package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.local.RouteRecordData
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.GetGymRouteInfoRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.customview.DeleteDialog
import com.climus.climeet.presentation.ui.main.global.selectsector.FloorBtnState
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import com.climus.climeet.presentation.ui.main.global.toGymLevelUiData
import com.climus.climeet.presentation.ui.main.global.toRouteUiData
import com.climus.climeet.presentation.ui.main.global.toSectorNameUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

data class CreateRecordUiState(
    val isSingleFloor: Boolean = false,
    val curFloor: Int = 0,
    val layoutImg: String? = "",
    val firstFloorBtnState: FloorBtnState = FloorBtnState.FloorSelected,
    val secondFloorBtnState: FloorBtnState = FloorBtnState.FloorUnSelected,
    val backgroundImage: String = "",
    val sectorNameList: List<SectorNameUiData> = emptyList(),
    val gymLevelList: List<GymLevelUiData> = emptyList(),
    val routeList: List<RouteUiData> = emptyList(),
    val selectedSector: SectorNameUiData = SectorNameUiData {},
    val selectedLevel: GymLevelUiData = GymLevelUiData {},
    val selectedRoute: RouteUiData = RouteUiData {},
    val selectedFilter: SelectedFilter = SelectedFilter(),
    val clearBtnState: Boolean = false
)

sealed class CreateRecordEvent {
    data class ShowToastMessage(val msg: String) : CreateRecordEvent()
}

@HiltViewModel
class SetTimerClimbingRecordViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRecordUiState())
    val uiState: StateFlow<CreateRecordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateRecordEvent>()
    val event: SharedFlow<CreateRecordEvent> = _event.asSharedFlow()

    private val _items = MutableStateFlow<List<RouteUiData>>(emptyList())
    val items: StateFlow<List<RouteUiData>> = _items.asStateFlow()
    val itemsLiveData: LiveData<List<RouteUiData>> = _items.asLiveData()

    var cragId: Long = 0
    var cragName: String = ""
    var apiCheck: Boolean = false

    private var sectorNameList = listOf<SectorNameUiData>()
    private var gymLevelList = listOf<GymLevelUiData>()

    val isSelectedCrag = MutableStateFlow(false)
    private val selectedCragEvent = MutableLiveData<Pair<Long, String>>()

    private val _challengeNumber = MutableLiveData(0)
    val challengeNumber: LiveData<Int> = _challengeNumber

    val isAvgToggleOn = MutableLiveData(true)
    val isRouteToggleOn = MutableLiveData(true)

    val alpha = MutableLiveData(1f)

    val totalRoute = MutableLiveData("--")
    val totalComplete = MutableLiveData("--")
    val avgLevel = MutableLiveData("--")
    val avgCompleteRate: MediatorLiveData<Double> = MediatorLiveData<Double>().apply {
        addSource(totalRoute) { recalculateAvgRate() }
        addSource(totalComplete) { recalculateAvgRate() }
    }

    // 루트기록 재설정용
    val updateRecord = MutableLiveData(false)   // 레벨 완등률 업데이트
    val isRestart = MutableLiveData(false)
    val resetView = MutableLiveData(false)
    private var isSecondFloorExist: Boolean = false

    private var maxFloor = 0
    private var firstFloorRouteList: List<RouteUiData> = emptyList()
    private var secondFloorRouteList: List<RouteUiData> = emptyList()

    private fun recalculateAvgRate() {
        if (totalRoute.value == "--" || totalComplete.value == "--") {
            avgCompleteRate.value = 0.0
        } else {
            val total = totalRoute.value?.toDoubleOrNull()
            val complete = totalComplete.value?.toDoubleOrNull()

            if (total != null && complete != null && total != 0.0) {
                avgCompleteRate.value = complete / total
            } else {
                avgCompleteRate.value = 0.0
            }
        }
    }

    init {
        setTotalValue()
    }

    private fun setTotalValue() {
        totalRoute.value = sharedPreferences.getString(TOP_CHALLENGE, "--")
        totalComplete.value = sharedPreferences.getString(TOP_COMPLETE, "--")
        avgLevel.value = sharedPreferences.getString(TOP_LEVEL, "--")
    }

    private fun getClimbingData() {

        CoroutineScope(Dispatchers.IO).launch {
            // Room DB에서 루트기록 가져오기
            val routeDataList = repository.getAllRoute()

            withContext(Dispatchers.Main) {
                if (routeDataList != null) {
                    isSelectedCrag.value = true

                    routeDataList.forEach { routeData ->
                        // API로 가져온 루트 정보 불러오기
                        var matchedData = findMatchedData(routeData.routeId, routeData.sectorId, 1)
                        if (matchedData != null) {
                            setItem(matchedData, routeData)
                        } else {
                            // 2층이 존재하면 2층 데이터를 가져와 일치 확인하고, 있으면 넣어주기
                            if (isSecondFloorExist) {
                                matchedData =
                                    findMatchedData(routeData.routeId, routeData.sectorId, 2)
                                if (matchedData != null) {
                                    setItem(matchedData, routeData)
                                }
                            }
                        }
                    }
                    //Log.d("recorddd", "재설정 끝 items\n${items.value}")
                    setAvgLevel()
                    resetView.value = true  // 일시정지 일 때 루트기록 보이게 화면 재설정
                }
            }
        }
    }

    private fun setItem(crag: RouteUiData, item: RouteRecordData) {
        crag.clearBtnState = item.isCompleted
        crag.challengeNum = item.attemptCount
        crag.isSelected = true

        // items에 추가
        _items.value = _items.value + crag
    }


    // routeList에서 일치하는 데이터를 return
    private fun findMatchedData(routeId: Long, sectorId: Long, floor: Int): RouteUiData? {
        return if (floor == 1) {
            firstFloorRouteList.firstOrNull { it.routeId == routeId && it.sectorId == sectorId }
        } else {
            secondFloorRouteList.firstOrNull { it.routeId == routeId && it.sectorId == sectorId }
        }
    }

    fun selectedCrag(id: Long, name: String) {
        if (!apiCheck) {
            selectedCragEvent.value = Pair(id, name)
            cragId = id
            cragName = name
            getCragInfo(cragId)
            apiCheck = true
        }
    }

    // 1. 암장의 섹터와 난이도 정보 가져오기 (총 몇층인기 저장)
    private fun getCragInfo(id: Long) {
        viewModelScope.launch {
            repository.getGymFilteringKey(id).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("recorddd", "암장 정보 가져오기 성공")
                        sectorNameList = it.body.sectorList.map { data ->
                            data.toSectorNameUiData(::selectSectorName)
                        }

                        gymLevelList = it.body.difficultyList.map { data ->
                            data.toGymLevelUiData(::selectGymLevel)
                        }

                        if (it.body.maxFloor == 1) {
                            _uiState.update { state ->
                                state.copy(
                                    isSingleFloor = true,
                                    layoutImg = it.body.layoutImageUrl
                                )
                            }
                            maxFloor = 1

                        } else {
                            _uiState.update { state ->
                                state.copy(
                                    isSingleFloor = false,
                                    layoutImg = it.body.layoutImageUrl
                                )
                            }
                            maxFloor = 2
                            setFloorInfo(2)
                            setFloorInfo(1)
                            isSecondFloorExist = true
                        }
                        setFloorInfo(1)
                    }

                    is BaseState.Error -> {
                        //Log.d("recorddd", "암장 정보 가져오기 실패")
                        _event.emit(CreateRecordEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    // [사용자 입력] 층 설정 -> 층에 맞는 sector만 필터링
    fun selectFloor(floor: Int) {
        _uiState.update { state ->
            state.copy(
                secondFloorBtnState = if (floor == 2) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected,
                firstFloorBtnState = if (floor == 1) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected,
                curFloor = floor,
                sectorNameList = sectorNameList.filter {
                    it.floor == floor
                },
                selectedRoute = RouteUiData {}
            )
        }
        setFloorInfo(floor)
    }

    // 2. 1번에서 호출 -> 가져온 정보를 층에 맞는 섹터, 레벨, 루트 보여주게 설정
    private fun setFloorInfo(floor: Int) {

        viewModelScope.launch {
            repository.getGymRouteInfoList(cragId, GetGymRouteInfoRequest(0, 10, floor)).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("recorddd", "루트정보 가져오기 성공")
                        _uiState.update { state ->
                            state.copy(
                                sectorNameList = sectorNameList.filter { data -> data.floor == floor },
                                gymLevelList = gymLevelList,
                                routeList = it.body.result.map { data ->
                                    data.toRouteUiData(::selectRoute)
                                }
                            )
                        }
                    }

                    is BaseState.Error -> {
                        Log.d("recorddd", "루트정보 가져오기 실패")
                        _event.emit(CreateRecordEvent.ShowToastMessage(it.msg))
                    }
                }
            }
            withContext(Dispatchers.Main) {
                // 각 층 루트 저장
                if (floor == 1 && firstFloorRouteList.isEmpty()) {
                    firstFloorRouteList = uiState.value.routeList
                } else if (floor == 2 && secondFloorRouteList.isEmpty()) {
                    secondFloorRouteList = uiState.value.routeList
                }

                if (maxFloor == 1) {
                    if (isRestart.value == true) {
                        //Log.d("recorddd", "getCragInfo에서 item 재설정 호출 (1층짜리)")
                        isRestart.value = false
                        getClimbingData()
                    }
                } else {
                    if (isRestart.value == true && floor == 2) {
                        //Log.d("recorddd", "getCragInfo에서 item 재설정 호출 (2층짜리)")
                        isRestart.value = false
                        getClimbingData()
                    }
                }
            }
        }
    }

    // 3. 4, 5에서 호출 (섹터, 레벨 선택) -> 해당 층, 섹터, 레벨을 가진 루트 정보 가져오기
    private fun getRouteList(
        floor: Int,
        sectorId: Long? = null,
        difficulty: Int? = null,
    ) {
        cragId = selectedCragEvent.value?.first ?: run { 0 }
        viewModelScope.launch {
            repository.getGymRouteInfoList(
                cragId, GetGymRouteInfoRequest(
                    0, 10, floor, sectorId, difficulty
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                routeList = it.body.result.map { data ->
                                    data.toRouteUiData(::selectRoute)
                                }
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _event.emit(CreateRecordEvent.ShowToastMessage(it.msg))
                    }
                }

            }
        }
        Log.d("recorddd", "routeList 업데이트")
    }

    // 4. 1에서 호출 -> 선택된 섹터의 레벨과 루트정보 보여주기
    private fun selectSectorName(item: SectorNameUiData) {

        _uiState.update { state ->
            state.copy(
                sectorNameList = state.sectorNameList.map {
                    it.copy(
                        isSelected = it.sectorId == item.sectorId
                    )
                },
                selectedSector = item,
                selectedRoute = RouteUiData {}
            )
        }

        getRouteList(
            uiState.value.curFloor,
            item.sectorId,
            if (uiState.value.selectedLevel.difficulty != -1) uiState.value.selectedLevel.difficulty else null
        )

    }

    // 5. 1에서 호출 -> 선택된 레벨의 루트 정보 보여주기
    private fun selectGymLevel(item: GymLevelUiData) {
        _uiState.update { state ->
            state.copy(
                gymLevelList = state.gymLevelList.map {
                    it.copy(
                        isSelected = it.difficulty == item.difficulty
                    )
                },
                selectedLevel = item,
                selectedRoute = RouteUiData {}
            )
        }

        getRouteList(
            uiState.value.curFloor,
            if (uiState.value.selectedSector.sectorId != -1L) uiState.value.selectedSector.sectorId else null,
            item.difficulty
        )
    }

    // 6. 2, 3에서 호출 (층, 루트 선택) ->  선택된 루트 저장
    fun selectRoute(item: RouteUiData) {
        _uiState.update { state ->
            state.copy(
                selectedRoute = item,
                clearBtnState = item.clearBtnState
            )
        }
        _challengeNumber.value = item.challengeNum

        addItem(item)
    }

    // 루트기록 item 클릭 시 RouteUiData에 저장된 challengeNum 가져옴
    fun setChallengeNum(count: Int) {
        _challengeNumber.value = count
    }

    // 상단 루트기록 도전 횟수 증가
    fun addChallengeNum() {
        _challengeNumber.value = (_challengeNumber.value ?: 0) + 1
        _items.value = _items.value.map {
            if (it.routeId == uiState.value.selectedRoute.routeId) it.copy(challengeNum = it.challengeNum + 1) else it
        }
        _items.value.filter { it.routeId == uiState.value.selectedRoute.routeId }.forEach {
            // roomDB 도전 횟수 증가
            setRoomChallengeNum(it, true)
        }
    }

    // 상단 루트기록 도전 횟수 감소
    fun subChallengeNum() {
        val currentValue = _challengeNumber.value ?: 0
        var check = false
        if (currentValue > 0) {
            _challengeNumber.value = currentValue - 1
            check = true
        }
        _items.value = _items.value.map {
            if (it.routeId == uiState.value.selectedRoute.routeId && it.challengeNum > 0) it.copy(
                challengeNum = it.challengeNum - 1
            ) else it
        }
        _items.value.filter { it.routeId == uiState.value.selectedRoute.routeId && check }.forEach {
            // roomDB 도전 횟수 감소
            setRoomChallengeNum(it, false)
        }
    }

    private fun animateImage() {
        val alphaAnimator = ValueAnimator.ofFloat(1f, 0f)

        alphaAnimator.duration = 2000

        alphaAnimator.addUpdateListener { animation ->
            alpha.postValue(animation.animatedValue as Float)
        }

        val animatorSet = AnimatorSet()
        animatorSet.play(alphaAnimator)
        animatorSet.duration = 2000
        animatorSet.start()
    }

    // 완등 버튼 상태 (위에서 눌렀을 때)
    fun setClear() {
        _uiState.update { state ->
            state.copy(
                clearBtnState = !state.clearBtnState
            )
        }

        _items.value = _items.value.map {
            if (it.routeId == uiState.value.selectedRoute.routeId) {
                it.copy(
                    clearBtnState = !it.clearBtnState
                )
            } else it
        }

        if (uiState.value.clearBtnState) {
            animateImage()
        }

        // 루트 기록 완등 여부 반영
        _items.value.filter { it.routeId == uiState.value.selectedRoute.routeId }.forEach {
            setComplete(it, uiState.value.clearBtnState)
            setAvgLevel()
        }
    }

    // 루트 기록에 추가
    private fun addItem(item: RouteUiData) {
        if (_items.value.none { it.routeId == item.routeId && it.sectorId == item.sectorId }) {
            val newItem = RouteUiData(
                routeId = item.routeId,
                sectorId = item.sectorId,
                sectorName = item.sectorName,
                difficulty = item.difficulty,
                gymLevelName = item.gymLevelName,
                gymLevelColor = item.gymLevelColor,
                routeImg = item.routeImg,
                isSelected = true,
                onClickListener = { it -> itemClicked(it) }
            )
            _items.value = _items.value + newItem

            // roomDB에 루트 기록 저장
            saveRouteRecord(newItem)
            setAvgLevel()
        } else {
        }
    }

    // 토글 루트 기록 도전 수 증가
    fun itemIncrease(id: Long, num: Int) {
        _items.value = _items.value.map {
            if (it.routeId == id) {
                if (uiState.value.selectedRoute.routeId == it.routeId) {
                    _challengeNumber.value = (_challengeNumber.value ?: 0) + num
                }
                it.copy(challengeNum = it.challengeNum + num)
            } else it
        }
        _items.value.filter { it.routeId == id }.forEach {
            // roomDB 도전 횟수 증가
            setRoomChallengeNum(it, true)
        }
    }

    // 토글 루트 기록 도전 수 감소
    fun itemDecrease(id: Long) {
        var check = false
        _items.value = _items.value.map {
            if (it.routeId == id && it.challengeNum > 0) {
                if (uiState.value.selectedRoute.routeId == it.routeId) {
                    _challengeNumber.value = (_challengeNumber.value ?: 0) - 1
                    check = true
                }
                it.copy(challengeNum = it.challengeNum - 1)
            } else it
        }
        _items.value.filter { it.routeId == id && check }.forEach {
            // roomDB 도전 횟수 감소
            setRoomChallengeNum(it, false)
        }
    }

    // 삭제 다이얼로그 띄우기
    fun showDeleteDialog(context: Context, id: Long) {
        val dialog = DeleteDialog(context) { isDelete ->
            if (isDelete) {
                removeItem(id)
            }
        }
        dialog.show()
    }

    // 루트 기록 삭제
    private fun removeItem(id: Long) {
        val itemToRemove = _items.value.find { it.routeId == id }
        if (itemToRemove != null) {
            // roomDB 루트 기록 삭제
            deleteRouteRecord(itemToRemove, itemToRemove.clearBtnState)
            setAvgLevel()
        }
        _items.value = _items.value.filter { it.routeId != id }
        _uiState.update { state ->
            state.copy(
                selectedRoute = RouteUiData {}
            )
        }
    }

    // 완등 버튼 상태 (토글에서 눌렀을 때)
    fun setBtnState(id: Long) {
        _items.value = _items.value.map {
            if (it.routeId == id) {
                if (uiState.value.selectedRoute.routeId == it.routeId) {
                    _uiState.update { state ->
                        state.copy(
                            clearBtnState = !state.clearBtnState
                        )
                    }
                }
                it.copy(
                    clearBtnState = !it.clearBtnState
                )
            } else it
        }

        if (uiState.value.clearBtnState) {
            animateImage()
        }

        // 루트 기록 완등 여부 반영
        _items.value.filter { it.routeId == uiState.value.selectedRoute.routeId }.forEach {
            setComplete(it, uiState.value.clearBtnState)
            setAvgLevel()
        }
    }

    private fun itemClicked(id: RouteUiData) {

    }

    // 평균 완등률 더보기
    fun setAvgToggle() {
        isAvgToggleOn.value = !(isAvgToggleOn.value ?: false)
    }

    // 루트기록 더보기
    fun setRouteToggle() {
        isRouteToggleOn.value = !(isRouteToggleOn.value ?: false)
    }

    // uiState, items 초기화
    fun resetAtStop() {
        _uiState.value = CreateRecordUiState()
        _items.value = emptyList()
    }

    // -------- room DB ----------
    private fun saveRouteRecord(item: RouteUiData) {
        // 중복된 값 없을 때만 루트 기록 저장
        CoroutineScope(Dispatchers.IO).launch {
            val existCheck = repository.findExistRoute(item.sectorId, item.routeId)
            if (existCheck == null) {
                val routeRecord = RouteRecordData(
                    sectorId = item.sectorId,
                    routeId = item.routeId,
                    levelName = item.gymLevelName,
                    levelColor = item.gymLevelColor,
                    difficulty = item.difficulty,
                    attemptCount = 0,
                    isCompleted = false
                )
                repository.insert(routeRecord)

                delay(500)
                withContext(Dispatchers.Main) {
                    updateRecord.value = !updateRecord.value!!
                }
            }
        }

        // 상단 총 도전 수 업데이트
        var num = totalRoute.value
        if (num != null) {
            num = if (totalRoute.value == "--") {
                "1"
            } else {
                (num.toInt() + 1).toString()
            }
            totalRoute.value = num
            sharedPreferences.edit().putString(TOP_CHALLENGE, num).apply()
        }
    }

    private fun setRoomChallengeNum(item: RouteUiData, plus: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (plus) {
                // 도전 횟수 증가
                val record = repository.findExistRoute(item.sectorId, item.routeId)
                if (record != null) {
                    record.attemptCount += 1
                    repository.update(record)
                    //Log.d("recorddd", "도전 횟수 증가 db 반영")
                }
            } else {
                // 도전 횟수 감소
                val record = repository.findExistRoute(item.sectorId, item.routeId)
                if (record != null) {
                    record.attemptCount -= 1
                    repository.update(record)
                    //Log.d("recorddd", "도전 횟수 감소 db 반영")
                }
            }
        }
    }

    private fun setComplete(item: RouteUiData, complete: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val record = repository.findExistRoute(item.sectorId, item.routeId)
            if (record != null) {
                if (complete) {
                    // 루트 완등
                    record.isCompleted = true
                    repository.update(record)

                    delay(500)
                    withContext(Dispatchers.Main) {
                        updateRecord.value = !updateRecord.value!!
                    }
                    //Log.d("recorddd", "id ${record.id} 루트 완등")
                } else {
                    // 루트 완등 취소
                    record.isCompleted = false
                    repository.update(record)

                    delay(500)
                    withContext(Dispatchers.Main) {
                        updateRecord.value = !updateRecord.value!!
                    }
                    //Log.d("recorddd", "id ${record.id} 루트 완등 취소")
                }
            }
        }

        if (complete) {
            // 상단 총 완등 수 업데이트
            var num = totalComplete.value
            if (num != null) {
                num = if (totalComplete.value == "--") {
                    "1"
                } else {
                    (num.toInt() + 1).toString()
                }
                totalComplete.value = num
                sharedPreferences.edit().putString(TOP_COMPLETE, num).apply()
            }
        } else {
            // 상단 총 완등 수 업데이트
            var num = totalComplete.value
            if (num != null) {
                num = if (totalComplete.value == "1" || totalComplete.value == "0") {
                    "--"
                } else {
                    (num.toInt() - 1).toString()
                }
                totalComplete.value = num
                sharedPreferences.edit().putString(TOP_COMPLETE, num).apply()
            }
        }
    }

    private fun deleteRouteRecord(item: RouteUiData, isCompleted: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val target = repository.findExistRoute(item.sectorId, item.routeId)
            if (target != null) {
                // 루트 기록 삭제
                repository.deleteById(target.id)

                delay(500)
                withContext(Dispatchers.Main) {
                    updateRecord.value = !updateRecord.value!!
                }
                //Log.d("recorddd", "id ${target.id} 루트 기록 삭제")
            }
        }

        // 상단 총 도전 수 업데이트
        var num = totalRoute.value
        if (num != null) {
            num = if (totalRoute.value == "1" || totalRoute.value == "0") {
                "--"
            } else {
                (num.toInt() - 1).toString()
            }
            totalRoute.value = num
            sharedPreferences.edit().putString(TOP_CHALLENGE, num).apply()

            // 완등한 루트 기록이 삭제되면 완등 수도 업데이트
            if (isCompleted) {
                var num = totalComplete.value
                if (num != null) {
                    num = if (totalComplete.value == "1" || totalComplete.value == "0") {
                        "--"
                    } else {
                        (num.toInt() - 1).toString()
                    }
                    totalComplete.value = num
                    sharedPreferences.edit().putString(TOP_COMPLETE, num).apply()
                }
            }
        }
    }

    // 상단 평균 레벨 설정
    private fun setAvgLevel() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            val completedRoutes = repository.getAverageDifficultyOfCompleted().roundToInt()
            withContext(Dispatchers.Main) {
                if (completedRoutes != null && totalComplete.value != "--") {
                    // 난이도 이름과 매칭
                    avgLevel.value = gymLevelList[completedRoutes].levelName
                    sharedPreferences.edit().putString(TOP_LEVEL, avgLevel.value).apply()
                } else {
                    avgLevel.value = "--"
                }
                //Log.d("recorddd", "평균 레벨 계산: $completedRoutes ${avgLevel.value}")
            }
        }
    }

    companion object {
        const val TOP_CHALLENGE = "timerChallenge"
        const val TOP_COMPLETE = "timerComplete"
        const val TOP_LEVEL = "timerLevel"
    }
}