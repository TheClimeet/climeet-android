package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.GetGymRouteInfoRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.selectsector.FloorBtnState
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import com.climus.climeet.presentation.ui.main.global.toGymLevelUiData
import com.climus.climeet.presentation.ui.main.global.toRouteUiData
import com.climus.climeet.presentation.ui.main.global.toSectorNameUiData
import com.climus.climeet.presentation.ui.main.record.model.RouteRecordUiData
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordRepository
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val repository: MainRepository,
    private val climbingRepository: ClimbingRecordRepository,
    private val routeRepository: RouteRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRecordUiState())
    val uiState: StateFlow<CreateRecordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateRecordEvent>()
    val event: SharedFlow<CreateRecordEvent> = _event.asSharedFlow()

    private val _items = MutableStateFlow<List<RouteRecordUiData>>(emptyList())
    val items: StateFlow<List<RouteRecordUiData>> = _items.asStateFlow()
    val itemsLiveData: LiveData<List<RouteRecordUiData>> = _items.asLiveData()

    var cragId: Long = 0
    var cragName: String = ""

    private var sectorNameList = listOf<SectorNameUiData>()
    private var gymLevelList = listOf<GymLevelUiData>()

    val isSelectedCrag = MutableStateFlow(false)
    val selectedCragEvent = MutableLiveData<Pair<Long, String>>()

    private val _challengeNumber = MutableLiveData(0)
    val challengeNumber: LiveData<Int> = _challengeNumber

    val isAvgToggleOn = MutableLiveData(true)
    val isRouteToggleOn = MutableLiveData(true)

    init {
        getClimbingData()
    }

    private fun getClimbingData() {
        //val climbingData = climbingRepository.getAll()
        // todo : db에서 루트기록 데이터 가져와 루트기록 더보기에 보여주기
    }

    fun selectedCrag(id: Long, name: String) {
        selectedCragEvent.value = Pair(id, name)
        cragId = id
        cragName = name
        getCragInfo(cragId)
    }

    // 1. 암장의 섹터와 난이도 정보 가져오기 (총 몇층인기 저장)
    private fun getCragInfo(id: Long) {
        viewModelScope.launch {
            repository.getGymFilteringKey(id).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("seon", "암장 정보 가져오기 성공")
                        sectorNameList = it.body.sectorList.map { data ->
                            data.toSectorNameUiData(::selectSectorName)
                        }

                        gymLevelList = it.body.difficultyList.map { data ->
                            data.toGymLevelUiData(::selectGymLevel)
                        }

                        if (it.body.maxFloor == 1) {
                            Log.d("seon", "암장 정보 1층")
                            _uiState.update { state ->
                                state.copy(
                                    isSingleFloor = true,
                                    layoutImg = it.body.layoutImageUrl
                                )
                            }
                        } else {
                            Log.d("seon", "암장 정보 2층")
                            _uiState.update { state ->
                                state.copy(
                                    isSingleFloor = false,
                                    layoutImg = it.body.layoutImageUrl
                                )
                            }
                        }

                        setFloorInfo(1)
                    }

                    is BaseState.Error -> {
                        Log.d("seon", "암장 정보 가져오기 실패")
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
                selectedRoute = RouteUiData{}
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
                        Log.d("seon", "루트정보 가져오기 성공")
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
                        Log.d("seon", "루트정보 가져오기 실패")
                        _event.emit(CreateRecordEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    // 3. 4에서 호출 -> 해당 층, 섹터, 난이도의 루트 정보들 가져오기
    private fun getRouteList(
        floor: Int,
        sectorId: Long? = null,
        difficulty: Int? = null,
    ) {
        cragId = selectedCragEvent.value?.let { it.first } ?: run { 0 }
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
    private fun selectRoute(item: RouteUiData) {
        _uiState.update { state ->
            state.copy(
                selectedRoute = item
            )
        }
    }

    fun addChallengeNum() {
        _challengeNumber.value = (_challengeNumber.value ?: 0) + 1
        Log.d("TIMER", "add")
    }

    fun subChallengeNum() {
        val currentValue = _challengeNumber.value ?: 0
        if (currentValue > 0) {
            _challengeNumber.value = currentValue - 1
        }
        Log.d("TIMER", "sub")
    }

    fun setClear() {
        _uiState.update { state ->
            state.copy(
                clearBtnState = !state.clearBtnState
            )
        }
    }

    // 루트 기록에 추가?
    fun addItem(item: RouteUiData) {
        Log.d("TIMER", "itemcheck : " + _items.value.toString())
        if (_items.value.none { it.sectorId == item.sectorId }) {
            val newItem = RouteRecordUiData(
                sectorId = item.sectorId,
                sectorName = item.sectorName,
                levelName = item.gymLevelName,
                levelColor = item.gymLevelColor,
                sectorImg = item.routeImg,
                onClickListener = { id -> itemClicked(id) }
            )
            _items.value = _items.value + newItem
        } else {

        }
    }

    // 루트기록 수 증가
    fun itemIncrease(id: Long) {
        _items.value = _items.value.map {
            if (it.sectorId == id) it.copy(challengeNum = it.challengeNum + 1) else it
        }
    }

    // 루트 기록 수 감소
    fun itemDecrease(id: Long) {
        _items.value = _items.value.map {
            if (it.sectorId == id && it.challengeNum > 0) it.copy(challengeNum = it.challengeNum - 1) else it
        }
    }

    // 루트 기록 삭제
    fun removeItem(id: Long) {
        _items.value = _items.value.filter { it.sectorId != id }
    }

    private fun itemClicked(id: Long) {
        // 여기에 아이템 클릭 시 실행할 코드를 추가하세요
    }

    // 평균 완등률 더보기
    fun setAvgToggle() {
        isAvgToggleOn.value = !(isAvgToggleOn.value ?: false)
    }

    // 루트기록 더보기
    fun setRouteToggle() {
        isRouteToggleOn.value = !(isRouteToggleOn.value ?: false)
    }

    // todo : 루트기록 추가되면 roomDB에 저장

    // todo : 루트기록 지워지면 roomDB에서 삭제
}
