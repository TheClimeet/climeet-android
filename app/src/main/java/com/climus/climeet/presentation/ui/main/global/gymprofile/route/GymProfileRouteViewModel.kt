package com.climus.climeet.presentation.ui.main.global.gymprofile.route

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.app.App.Companion.sharedPreferences
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
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
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
import javax.inject.Inject

data class GymProfileSelectSectorUiState(
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
    val selectedFilter: SelectedFilter = SelectedFilter()
)

sealed class GymProfileRouteEvent {
    data object ShowDatePicker : GymProfileRouteEvent()
    data object deleteFilter: GymProfileRouteEvent()
    data class ApplyFilter(val filter: SelectedFilter) : GymProfileRouteEvent()
    data class ShowToastMessage(val msg: String) : GymProfileRouteEvent()
}

@HiltViewModel
class GymProfileRouteViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GymProfileSelectSectorUiState())
    val uiState: StateFlow<GymProfileSelectSectorUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<GymProfileRouteEvent>()
    val event: SharedFlow<GymProfileRouteEvent> = _event.asSharedFlow()

    private var sectorNameList = listOf<SectorNameUiData>()
    private var gymLevelList = listOf<GymLevelUiData>()

    val initDate = CreateRecordData.selectedDate
    val datePickText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일")
    val selectedDate = MutableLiveData(initDate)

    var cragId: Long = 0
    var cragName: String = ""

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun setCragInfo(id: Long, name: String) {
        cragId = id
        cragName = name
        Log.d("gym_profile", "암장 루트탭에서 아이디 : $cragId, 이름 : $cragName")
        getCragInfo(cragId)
    }

    fun setDate() {
        val date = selectedDate.value
        viewModelScope.launch(Dispatchers.Main) {
            val year = date?.year
            val month = date?.month?.value
            val day = date?.dayOfMonth
            datePickText.value = "${year}년 ${month}월 ${day}일"
        }
    }

    private fun getCragInfo(id: Long) {

        viewModelScope.launch {
            repository.getGymFilteringKey(id).let {
                when (it) {
                    is BaseState.Success -> {
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
                        } else {
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
                        _event.emit(GymProfileRouteEvent.ShowToastMessage(it.msg))
                    }
                }
            }

        }
    }

    fun selectFloor(floor: Int) {
        _uiState.update { state ->
            state.copy(
                secondFloorBtnState =if (floor == 2) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected,
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

    private fun setFloorInfo(floor: Int) {

        viewModelScope.launch {
            repository.getGymRouteInfoList(cragId, GetGymRouteInfoRequest(0, 10, floor)).let {
                when (it) {
                    is BaseState.Success -> {

                        _uiState.update { state ->
                            state.copy(
                                sectorNameList = sectorNameList.filter { data -> data.floor == floor },
                                gymLevelList = gymLevelList,
                                routeList = it.body.result.map { data ->
                                    data.toRouteUiData(::selectRoute)
                                },
                                curFloor = 1
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _event.emit(GymProfileRouteEvent.ShowToastMessage(it.msg))
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
                        _event.emit(GymProfileRouteEvent.ShowToastMessage(it.msg))
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
                selectedRoute = RouteUiData {}
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
                selectedRoute = RouteUiData {}
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
            cragId = cragId,
            routeId = uiState.value.selectedRoute.routeId,
            sectorId = uiState.value.selectedSector.sectorId,
            difficulty = uiState.value.selectedLevel.difficulty,
            sectorName = uiState.value.selectedRoute.sectorName.ifBlank { uiState.value.selectedSector.name },
            cragName = cragName,
            gymLevelName = uiState.value.selectedRoute.gymLevelName.ifBlank { uiState.value.selectedLevel.levelName }
        )
        viewModelScope.launch {
            _event.emit(
                GymProfileRouteEvent.ApplyFilter(
                    selectedFilter
                )
            )
        }
    }

    fun clearSelectedRoute() {
        _uiState.update { state ->
            state.copy(
                selectedRoute = RouteUiData {}
            )
        }

        viewModelScope.launch {
            _event.emit(GymProfileRouteEvent.deleteFilter)
        }
    }

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(GymProfileRouteEvent.ShowDatePicker)
        }
    }
}