package com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.GetGymRouteInfoRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm.cragName
import com.climus.climeet.presentation.ui.main.global.selectsector.FloorBtnState
import com.climus.climeet.presentation.ui.main.global.selectsector.SelectSectorBottomSheetEvent
import com.climus.climeet.presentation.ui.main.record.model.RouteRecordUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedFilter
import com.climus.climeet.presentation.ui.main.global.toGymLevelUiData
import com.climus.climeet.presentation.ui.main.global.toRouteUiData
import com.climus.climeet.presentation.ui.main.global.toSectorNameUiData
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import javax.inject.Inject

data class CreateClimbingRecordUiState(
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

sealed class CreateClimbingRecordEvent {
    data object ShowDatePicker : CreateClimbingRecordEvent()
    data object ShowTimePicker : CreateClimbingRecordEvent()

    data object NavigateToSelectCrag : CreateClimbingRecordEvent()

    data object NavigateToBack : CreateClimbingRecordEvent()

    data class ApplyFilter(val filter: SelectedFilter) : CreateClimbingRecordEvent()

    data class ShowToastMessage(val msg: String) : CreateClimbingRecordEvent()

}

@HiltViewModel
class CreateClimbingRecordViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateClimbingRecordUiState())
    val uiState: StateFlow<CreateClimbingRecordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateClimbingRecordEvent>()
    val event: SharedFlow<CreateClimbingRecordEvent> = _event.asSharedFlow()

    private val _items = MutableStateFlow<List<RouteRecordUiData>>(emptyList())
    val items: StateFlow<List<RouteRecordUiData>> = _items.asStateFlow()
    val itemsLiveData: LiveData<List<RouteRecordUiData>> = _items.asLiveData()

    val initDate = CreateRecordData.selectedDate
    val datePickText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일 (${initDate.dayOfWeek})")
    val selectedDate = MutableLiveData(initDate)

    val timePickText = MutableStateFlow("시간을 입력해주세요 (선택)")
    val selectedStartTime = MutableLiveData(CreateRecordData.selectedStartTime)
    val selectedEndTime = MutableLiveData(CreateRecordData.selectedEndTime)

    private var sectorNameList = listOf<SectorNameUiData>()
    private var gymLevelList = listOf<GymLevelUiData>()

    var cragId: Long = 0
    var cragName: String = ""

    val isSelectedCrag = MutableStateFlow(false)
    val selectedCragEvent = MutableLiveData<Pair<Long, String>>()

    private val _challengeNumber = MutableLiveData(0)
    val challengeNumber: LiveData<Int> = _challengeNumber

    val isToggleOn = MutableLiveData(true)

    init {
        selectCrag(0, "클라이밍 암장을 선택해주세요")
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun setSelectedTime(start: LocalTime, end: LocalTime) {
        selectedStartTime.value = start
        selectedEndTime.value = end
    }

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.ShowDatePicker)
        }
    }

    fun showTimePicker() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.ShowTimePicker)
        }
    }

    fun setDate() {
        val date = selectedDate.value
        viewModelScope.launch(Dispatchers.Main) {
            val koreanDayOfWeek = when (date?.dayOfWeek?.value) {
                1 -> "(월)"
                2 -> "(화)"
                3 -> "(수)"
                4 -> "(목)"
                5 -> "(금)"
                6 -> "(토)"
                7 -> "(일)"
                else -> throw IllegalArgumentException()
            }
            val year = date.year
            val month = date.month.value
            val day = date.dayOfMonth
            datePickText.value = "${year}년 ${month}월 ${day}일 $koreanDayOfWeek"
        }
    }

    fun setTime() {
        val start = selectedStartTime.value
        var startString = ""
        val end = selectedEndTime.value
        var endString = ""

        startString = if (start!!.hour < 12 || start!!.hour == 24) {
            if (start!!.hour == 24) {
                String.format(Locale.getDefault(), "AM %02d:%02d", 12, start.minute)
            } else {
                String.format(Locale.getDefault(), "AM %02d:%02d", start.hour, start.minute)
            }
        } else {
            if (start!!.hour == 12) {
                String.format(Locale.getDefault(), "PM %02d:%02d", 12, start.minute)
            } else {
                String.format(Locale.getDefault(), "PM %02d:%02d", start.hour, start.minute)
            }
        }

        endString = if (end!!.hour < 12 || end!!.hour == 24) {
            if (end!!.hour == 24) {
                String.format(Locale.getDefault(), "AM %02d:%02d", 12, end.minute)
            } else {
                String.format(Locale.getDefault(), "AM %02d:%02d", end.hour, end.minute)
            }
        } else {
            if (end!!.hour == 12) {
                String.format(Locale.getDefault(), "PM %02d:%02d", 12, end.minute)
            } else {
                String.format(Locale.getDefault(), "PM %02d:%02d", end.hour, end.minute)
            }
        }
        timePickText.value = "$startString - $endString"
    }

    fun selectCrag(id: Long, name: String) {
        selectedCragEvent.value = Pair(id, name)
        cragId = id
        cragName = name
        Log.d("testtt", "${selectedCragEvent.value}")
        selectedCragEvent.value?.let { getCragInfo(it.first) }
    }

    fun getCragInfo(id: Long) {
        viewModelScope.launch {
            repository.getGymFilteringKey(id).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("testtt", "성공은 함")
                        sectorNameList = it.body.sectorList.map { data ->
                            data.toSectorNameUiData(::selectSectorName)
                        }

                        gymLevelList = it.body.difficultyList.map { data ->
                            data.toGymLevelUiData(::selectGymLevel)
                        }

                        if (it.body.maxFloor == 1) {
                            Log.d("testtt", "1층")
                            _uiState.update { state ->
                                state.copy(
                                    isSingleFloor = true,
                                    layoutImg = it.body.layoutImageUrl
                                )
                            }
                        } else {
                            Log.d("testtt", "2층")
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
                        Log.d("testtt", "에러")
                        _event.emit(CreateClimbingRecordEvent.ShowToastMessage(it.msg))
                    }
                }
            }

        }
    }

    fun selectFloor(floor: Int) {
        _uiState.update { state ->
            Log.d("testtt", "설정 조져")
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
        Log.d("testtt", "${uiState.value}")
        setFloorInfo(floor)
    }

    private fun setFloorInfo(floor: Int) {

        viewModelScope.launch {
            repository.getGymRouteInfoList(cragId, GetGymRouteInfoRequest(0, 10, floor)).let {
                when (it) {
                    is BaseState.Success -> {
                        Log.d("testtt", "층 슬정 성공은 함")
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
                        _event.emit(CreateClimbingRecordEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun getRouteList(
        floor: Int,
        sectorId: Long? = null,
        difficulty: Int? = null,
    ) {
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
                        _event.emit(CreateClimbingRecordEvent.ShowToastMessage(it.msg))
                    }
                }

            }
        }
    }

    private fun selectSectorName(item: SectorNameUiData) {
        _uiState.update { state ->
            state.copy(
                sectorNameList = state.sectorNameList.map {
                    it.copy(
                        isSelected = it.sectorId == item.sectorId
                    )
                },
                selectedSector = item,
                selectedRoute = RouteUiData{}
            )
        }

        getRouteList(
            uiState.value.curFloor,
            item.sectorId,
            if (uiState.value.selectedLevel.difficulty != -1) uiState.value.selectedLevel.difficulty else null
        )

    }

    private fun selectGymLevel(item: GymLevelUiData) {
        _uiState.update { state ->
            state.copy(
                gymLevelList = state.gymLevelList.map {
                    it.copy(
                        isSelected = it.difficulty == item.difficulty
                    )
                },
                selectedLevel = item,
                selectedRoute = RouteUiData{}
            )
        }

        getRouteList(
            uiState.value.curFloor,
            if (uiState.value.selectedSector.sectorId != -1L) uiState.value.selectedSector.sectorId else null,
            item.difficulty
        )
    }

    private fun selectRoute(item: RouteUiData) {
        _uiState.update { state ->
            state.copy(
                selectedRoute = item
            )
        }
    }

    fun applySectorFilter() {
        val selectedFilter = SelectedFilter(
            routeId = uiState.value.selectedRoute.routeId,
            sectorId = uiState.value.selectedSector.sectorId,
            difficulty = uiState.value.selectedLevel.difficulty,
            sectorName = uiState.value.selectedSector.name,
            cragName = cragName,
            gymLevelName = uiState.value.selectedLevel.levelName
        )
        viewModelScope.launch {
            _event.emit(
                CreateClimbingRecordEvent.ApplyFilter(
                    selectedFilter
                )
            )
        }
    }

    fun addChallengeNum() {
        _challengeNumber.value = (_challengeNumber.value ?: 0) + 1
    }

    fun subChallengeNum() {
        val currentValue = _challengeNumber.value ?: 0
        if (currentValue > 0) {
            _challengeNumber.value = currentValue - 1
        }
    }

    fun setClear() {
        _uiState.update { state ->
            state.copy(
                clearBtnState = !state.clearBtnState
            )
        }
    }

    fun setToggle() {
        isToggleOn.value = !(isToggleOn.value ?: false)
    }

    fun addItem(item: RouteUiData) {
        Log.d("itemcheck", _items.value.toString())
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

    fun itemIncrease(id: Long) {
        _items.value = _items.value.map {
            if (it.sectorId == id) it.copy(challengeNum = it.challengeNum + 1) else it
        }
    }

    fun itemDecrease(id: Long) {
        _items.value = _items.value.map {
            if (it.sectorId == id && it.challengeNum > 0) it.copy(challengeNum = it.challengeNum - 1) else it
        }
    }

    fun removeItem(id: Long) {
        _items.value = _items.value.filter { it.sectorId != id }
    }

    private fun itemClicked(id: Long) {
        // 여기에 아이템 클릭 시 실행할 코드를 추가하세요
    }

    fun navigateToSelectCrag() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.NavigateToSelectCrag)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(CreateClimbingRecordEvent.NavigateToBack)
        }
    }

}