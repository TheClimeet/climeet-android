package com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.intro.signup.admin.AdminSignupForm.cragName
import com.climus.climeet.presentation.ui.main.global.selectsector.FloorBtnState
import com.climus.climeet.presentation.ui.main.record.model.RouteRecordUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
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
    val firstFloorBtnState: FloorBtnState = FloorBtnState.FloorSelected,
    val secondFloorBtnState: FloorBtnState = FloorBtnState.FloorUnSelected,
    val backgroundImage: String = "",
    val wallNameList: List<SectorNameUiData> = emptyList(),
    val sectorLevelList: List<GymLevelUiData> = emptyList(),
    val sectorImageList: List<RouteUiData> = emptyList(),
    val selectedSectorName: SectorNameUiData = SectorNameUiData {},
    val selectedSectorLevel: GymLevelUiData = GymLevelUiData {},
    val selectedSector: SelectedSector = SelectedSector(),
    val clearBtnState: Boolean = false
)

data class SelectedSector(
    val sectorId: Long = -1,
    val sectorName: String = "",
    val cragName: String = "",
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = "",
)

sealed class CreateClimbingRecordEvent {
    data object ShowDatePicker : CreateClimbingRecordEvent()
    data object ShowTimePicker : CreateClimbingRecordEvent()

    data object NavigateToSelectCrag : CreateClimbingRecordEvent()

    data object NavigateToBack : CreateClimbingRecordEvent()
}

@HiltViewModel
class CreateClimbingRecordViewModel @Inject constructor() : ViewModel() {
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
    }

    fun getCragInfo(id: Long) {

        viewModelScope.launch {
            //todo 암장 정보 가져오기
            // - floor 1개인지 두개인지 도 적용

            _uiState.update { state ->
                state.copy(
                    isSingleFloor = false
                )
            }

            setFloorInfo(1)
        }
    }

    fun selectFloor(floor: Int) {
        _uiState.update { state ->
            state.copy(
                secondFloorBtnState = if (floor == 2) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected,
                firstFloorBtnState = if (floor == 1) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected
            )
        }
        setFloorInfo(floor)
    }

    private fun setFloorInfo(floor: Int) {

        // todo floor 별 초기데이터 삽입
        // todo sectorImageList 는 인기순 10개 최초로 받아오기

        _uiState.update { state ->
            state.copy(
                wallNameList = listOf(
                    SectorNameUiData(0,name = "Cheesegrater", onClickListener = ::selectWallName),
                    SectorNameUiData(1, name = "Jaws", onClickListener = ::selectWallName),
                    SectorNameUiData(2,name = "The Wallus", onClickListener = ::selectWallName),
                ),
                sectorLevelList = listOf(
                    GymLevelUiData("VB", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V1", "#FFFFFF", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V2", "#DDDDDD", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V3", "#CCCCCC", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V4", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V5", "#EEEEEE", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V6", "#555555", onClickListener = ::selectSectorLevel),
                ),
                sectorImageList = listOf(
                    RouteUiData(
                        0,
                        sectorName = "SECTOR 2-2",
                        gymLevelName = "VB",
                        gymLevelColor = "#BBBBBB",
                        routeImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RouteUiData(
                        1,
                        sectorName = "SECTOR 2-2",
                        gymLevelName = "V2",
                        gymLevelColor = "#456213",
                        routeImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RouteUiData(
                        2,
                        sectorName = "SECTOR 2-2",
                        gymLevelName = "V3",
                        gymLevelColor = "#BBBBBB",
                        routeImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RouteUiData(
                        3,
                        sectorName = "SECTOR 2-2",
                        gymLevelName = "V4",
                        gymLevelColor = "#456213",
                        routeImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RouteUiData(
                        4,
                        sectorName = "SECTOR 2-2",
                        gymLevelName = "V8",
                        gymLevelColor = "#BBBBBB",
                        routeImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    )
                )
            )
        }
    }

    private fun selectWallName(sectorId: Long) {

        _uiState.update { state ->
            state.copy(
                wallNameList = state.wallNameList.map {
                    it.copy(
                        isSelected = it.sectorId == sectorId
                    )
                },
                selectedSectorName = state.wallNameList.filter {
                    it.sectorId == sectorId
                }[0]
            )
        }

        viewModelScope.launch {
            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }

    }

    private fun selectSectorLevel(difficulty: Int) {
        _uiState.update { state ->
            state.copy(
                sectorLevelList = state.sectorLevelList.map {
                    it.copy(
                        isSelected = it.difficulty == difficulty
                    )
                },
                selectedSectorLevel = state.sectorLevelList.filter {
                    it.difficulty == difficulty
                }[0]
            )
        }

        viewModelScope.launch {

            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }
    }

    private fun selectSectorImage(id: Long) {
        var selectedData = SelectedSector()
        _uiState.update { state ->
            state.copy(
                sectorImageList = state.sectorImageList.map {
                    if (it.sectorId == id) {
                        selectedData =
                            SelectedSector(
                                it.sectorId,
                                it.sectorName,
                                cragName,
                                it.gymLevelName,
                                it.gymLevelColor,
                                it.routeImg
                            )
                        it.copy(
                            isSelected = true
                        )
                    } else {
                        it.copy(
                            isSelected = false
                        )
                    }

                },
                selectedSector = selectedData
            )
        }
        addItem(selectedData)
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

    fun addItem(item: SelectedSector) {
        Log.d("itemcheck", _items.value.toString())
        if (_items.value.none { it.sectorId == item.sectorId }) {
            val newItem = RouteRecordUiData(
                sectorId = item.sectorId,
                sectorName = item.sectorName,
                levelName = item.levelName,
                levelColor = item.levelColor,
                sectorImg = item.sectorImg,
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