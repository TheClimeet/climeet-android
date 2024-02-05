package com.climus.climeet.presentation.ui.main.global.selectsector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.GetGymRouteInfoRequest
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SelectedRoute
import com.climus.climeet.presentation.ui.main.global.toGymLevelUiData
import com.climus.climeet.presentation.ui.main.global.toRouteUiData
import com.climus.climeet.presentation.ui.main.global.toSectorNameUiData
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


data class SelectSectorBottomSheetUiState(
    val isSingleFloor: Boolean = false,
    val curFloor: Int = 0,
    val layoutImg: String? = "",
    val firstFloorBtnState: FloorBtnState = FloorBtnState.FloorSelected,
    val secondFloorBtnState: FloorBtnState = FloorBtnState.FloorUnSelected,
    val backgroundImage: String = "",
    val sectorNameList: List<SectorNameUiData> = emptyList(),
    val gymLevelList: List<GymLevelUiData> = emptyList(),
    val routeList: List<RouteUiData> = emptyList(),
    val selectedSectorId: Long = -1,
    val selectedDifficulty: Int = -1,
    val selectedRoute: SelectedRoute = SelectedRoute()
)

sealed class FloorBtnState {
    data object FloorSelected : FloorBtnState()
    data object FloorUnSelected : FloorBtnState()
}

sealed class SelectSectorBottomSheetEvent {
    data object NavigateToBack : SelectSectorBottomSheetEvent()
    data class ApplyFilter(val sector: SelectedRoute) :
        SelectSectorBottomSheetEvent()

    data object DismissDialog : SelectSectorBottomSheetEvent()
    data class ShowToastMessage(val msg: String) : SelectSectorBottomSheetEvent()
}

@HiltViewModel
class SelectSectorBottomSheetViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectSectorBottomSheetUiState())
    val uiState: StateFlow<SelectSectorBottomSheetUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SelectSectorBottomSheetEvent>()
    val event: SharedFlow<SelectSectorBottomSheetEvent> = _event.asSharedFlow()

    private var sectorNameList = listOf<SectorNameUiData>()
    private var gymLevelList = listOf<GymLevelUiData>()

    var cragId: Long = 0
    var cragName: String = ""

    fun setCragInfo(id: Long, name: String) {
        cragId = id
        cragName = name
        getCragInfo(cragId)
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(SelectSectorBottomSheetEvent.NavigateToBack)
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

                        if(it.body.maxFloor == 1){
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
                        _event.emit(SelectSectorBottomSheetEvent.ShowToastMessage(it.msg))
                    }
                }
            }

        }
    }

    fun selectFloor(floor: Int) {
        _uiState.update { state ->
            state.copy(
                secondFloorBtnState = if (floor == 2) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected,
                firstFloorBtnState = if (floor == 1) FloorBtnState.FloorSelected else FloorBtnState.FloorUnSelected,
                curFloor = floor,
                sectorNameList = sectorNameList.filter {
                    it.floor == floor
                }
            )
        }
        setFloorInfo(floor)
    }

    private fun setFloorInfo(floor: Int) {

        viewModelScope.launch {
            repository.getGymRouteInfoList(cragId, GetGymRouteInfoRequest(0, 10)).let { it ->
                when (it) {
                    is BaseState.Success -> {

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
                        _event.emit(SelectSectorBottomSheetEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun getRouteList(
        floor: Int,
        sectorId: Long?,
        difficulty: Int?,
    ) {
        viewModelScope.launch {
            repository.getGymRouteInfoList(
                cragId, GetGymRouteInfoRequest(
                    0, 10, floor, sectorId, difficulty
                )
            ).let {
                when(it){
                    is BaseState.Success -> {

                    }

                    is BaseState.Error -> {

                    }
                }

            }
        }
    }

    private fun selectSectorName(sectorId: Long) {

        _uiState.update { state ->
            state.copy(
                sectorNameList = state.sectorNameList.map {
                    it.copy(
                        isSelected = it.sectorId == sectorId
                    )
                },
                selectedSectorId = sectorId
            )
        }

        viewModelScope.launch {
            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }

    }

    private fun selectGymLevel(difficulty: Int) {
        _uiState.update { state ->
            state.copy(
                gymLevelList = state.gymLevelList.map {
                    it.copy(
                        isSelected = it.difficulty == difficulty
                    )
                },
                selectedDifficulty = difficulty
            )
        }

        viewModelScope.launch {

            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }
    }

    private fun selectRoute(id: Long) {
        var selectedData = SelectedRoute()
        _uiState.update { state ->
            state.copy(
                routeList = state.routeList.map {
                    if (it.sectorId == id) {
                        selectedData =
                            SelectedRoute(
                                routeId = it.routeId,
                                sectorId = it.sectorId,
                                sectorName = it.sectorName,
                                cragName = cragName,
                                gymLevelName = it.gymLevelName,
                                gymLevelColor = it.gymLevelColor,
                                climeetLevelName = it.climeetLevelName,
                                routeImg = it.routeImg
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
                selectedRoute = selectedData
            )
        }
    }

    fun applySectorFilter() {
        viewModelScope.launch {
            _event.emit(
                SelectSectorBottomSheetEvent.ApplyFilter(
                    uiState.value.selectedRoute
                )
            )
        }
    }

    fun dismissDialog() {
        viewModelScope.launch {
            _event.emit(SelectSectorBottomSheetEvent.DismissDialog)
        }
    }
}