package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.selectsector.FloorBtnState
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
import com.climus.climeet.presentation.ui.main.record.model.RouteRecordUiData
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordRepository
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordRepository
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

data class AddRecordUiState(
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

@HiltViewModel
class SetTimerClimbingRecordViewModel @Inject constructor(
    private val repository: MainRepository,
    private val climbingRepository: ClimbingRecordRepository,
    private val routeRepository: RouteRecordRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AddRecordUiState())
    val uiState: StateFlow<AddRecordUiState> = _uiState.asStateFlow()

    private val _items = MutableStateFlow<List<RouteRecordUiData>>(emptyList())
    val items: StateFlow<List<RouteRecordUiData>> = _items.asStateFlow()
    val itemsLiveData: LiveData<List<RouteRecordUiData>> = _items.asLiveData()

    val isSelectedCrag = MutableStateFlow(false)
    val selectedCragEvent = MutableLiveData<Pair<Long, String>>()

    private val _challengeNumber = MutableLiveData(0)
    val challengeNumber: LiveData<Int> = _challengeNumber

    val isAvgToggleOn = MutableLiveData(true)
    val isRouteToggleOn = MutableLiveData(true)


    private fun getData(){
        val climbingData = climbingRepository.getAll()
        // todo : db에서 루트기록 데이터 가져오기
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
                    SectorNameUiData(0, "Cheesegrater", onClickListener = ::selectWallName),
                    SectorNameUiData(1, "Jaws", onClickListener = ::selectWallName),
                    SectorNameUiData(2, "The Wallus", onClickListener = ::selectWallName),
                ),
                sectorLevelList = listOf(
                    GymLevelUiData("VB", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V1", "#FFFFFF", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V2", "#DDDDDD", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V3", "#CCCCCC", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V4", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V5", "#EEEEEE", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V6", "#555555", onClickListener = ::selectSectorLevel),
                    GymLevelUiData("V7", "#a3f0ff", onClickListener = ::selectSectorLevel),
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

    private fun selectWallName(item: SectorNameUiData) {

        _uiState.update { state ->
            state.copy(
                wallNameList = state.wallNameList.map {
                    it.copy(
                        isSelected = it.sectorId == item.sectorId
                    )
                },
                selectedSectorName = state.wallNameList.filter {
                    it.sectorId == item.sectorId
                }[0]
            )
        }

        viewModelScope.launch {
            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }

    }

    private fun selectSectorLevel(item: GymLevelUiData) {
        _uiState.update { state ->
            state.copy(
                sectorLevelList = state.sectorLevelList.map {
                    it.copy(
                        isSelected = it.difficulty == item.difficulty
                    )
                },
                selectedSectorLevel = state.sectorLevelList.filter {
                    it.difficulty == item.difficulty
                }[0]
            )
        }

        viewModelScope.launch {

            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }
    }

    private fun selectSectorImage(item: RouteUiData) {
        var selectedData = SelectedSector()
        _uiState.update { state ->
            state.copy(
                sectorImageList = state.sectorImageList.map {
                    if (it.sectorId == item.sectorId) {
                        selectedData =
                            SelectedSector(
                                it.sectorId,
                                it.sectorName,
                                selectedCragEvent.value?.second.toString(),
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
        Log.d("timer", "add")
    }

    fun subChallengeNum() {
        val currentValue = _challengeNumber.value ?: 0
        if (currentValue > 0) {
            _challengeNumber.value = currentValue - 1
        }
        Log.d("timer", "sub")
    }

    fun setClear() {
        _uiState.update { state ->
            state.copy(
                clearBtnState = !state.clearBtnState
            )
        }
    }

    fun addItem(item: SelectedSector) {
        Log.d("timer", "itemcheck : " + _items.value.toString())
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

    // 평균 완등률 더보기
    fun setAvgToggle() {
        isAvgToggleOn.value = !(isAvgToggleOn.value ?: false)
    }

    // 루트기록 더보기
    fun setRouteToggle() {
        isRouteToggleOn.value = !(isRouteToggleOn.value ?: false)
    }

    private fun createClimbingRecord(gymId: Long, date: String, time: LocalTime, avgDifficulty: Int){
        viewModelScope.launch {

        }
    }
}
