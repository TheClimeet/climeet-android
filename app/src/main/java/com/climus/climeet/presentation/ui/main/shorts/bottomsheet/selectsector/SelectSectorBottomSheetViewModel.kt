package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.shorts.model.SectorImageUiData
import com.climus.climeet.presentation.ui.main.shorts.model.SectorLevelUiData
import com.climus.climeet.presentation.ui.main.shorts.model.WallNameUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
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
    val firstFloorBtnState: FloorBtnState = FloorBtnState.FloorSelected,
    val secondFloorBtnState: FloorBtnState = FloorBtnState.FloorUnSelected,
    val backgroundImage: String = "",
    val wallNameList: List<WallNameUiData> = emptyList(),
    val sectorLevelList: List<SectorLevelUiData> = emptyList(),
    val sectorImageList: List<SectorImageUiData> = emptyList(),
    val selectedSectorName: WallNameUiData = WallNameUiData {},
    val selectedSectorLevel: SectorLevelUiData = SectorLevelUiData {},
    val selectedSector: SelectedSector = SelectedSector()
)

data class SelectedSector(
    val sectorId: Long = -1,
    val sectorName: String = "",
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = "",
)

sealed class FloorBtnState {
    data object FloorSelected : FloorBtnState()
    data object FloorUnSelected : FloorBtnState()
}

sealed class SelectSectorBottomSheetEvent {
    data object NavigateToBack : SelectSectorBottomSheetEvent()
    data class ApplyFilter(val sector: SelectedSector) :
        SelectSectorBottomSheetEvent()
}

@HiltViewModel
class SelectSectorBottomSheetViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectSectorBottomSheetUiState())
    val uiState: StateFlow<SelectSectorBottomSheetUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SelectSectorBottomSheetEvent>()
    val event: SharedFlow<SelectSectorBottomSheetEvent> = _event.asSharedFlow()

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
            // todo 암장 정보 가져오기

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
                    WallNameUiData("Cheesegrater", onClickListener = ::selectWallName),
                    WallNameUiData("Jaws", onClickListener = ::selectWallName),
                    WallNameUiData("The Wallus", onClickListener = ::selectWallName),
                ),
                sectorLevelList = listOf(
                    SectorLevelUiData("VB", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    SectorLevelUiData("V1", "#FFFFFF", onClickListener = ::selectSectorLevel),
                    SectorLevelUiData("V2", "#DDDDDD", onClickListener = ::selectSectorLevel),
                    SectorLevelUiData("V3", "#CCCCCC", onClickListener = ::selectSectorLevel),
                    SectorLevelUiData("V4", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    SectorLevelUiData("V5", "#EEEEEE", onClickListener = ::selectSectorLevel),
                    SectorLevelUiData("V6", "#555555", onClickListener = ::selectSectorLevel),
                ),
                sectorImageList = listOf(
                    SectorImageUiData(
                        0,
                        sectorName = "SECTOR 2-2",
                        levelName = "VB",
                        levelColor = "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    SectorImageUiData(
                        1,
                        sectorName = "SECTOR 2-2",
                        levelName = "V2",
                        levelColor = "#456213",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    SectorImageUiData(
                        2,
                        sectorName = "SECTOR 2-2",
                        levelName = "V3",
                        levelColor = "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    SectorImageUiData(
                        3,
                        sectorName = "SECTOR 2-2",
                        levelName = "V4",
                        levelColor = "#456213",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    SectorImageUiData(
                        4,
                        sectorName = "SECTOR 2-2",
                        levelName = "V8",
                        levelColor = "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    )
                )
            )
        }
    }

    private fun selectWallName(name: String) {

        _uiState.update { state ->
            state.copy(
                wallNameList = state.wallNameList.map {
                    it.copy(
                        isSelected = it.name == name
                    )
                },
                selectedSectorName = state.wallNameList.filter {
                    it.name == name
                }[0]
            )
        }

        viewModelScope.launch {
            //todo sectorImage 목록 최신화
            // - 기존 선택되어있는게 있으면, 그거 맨 앞으로 보내고, list 추가하기
        }

    }

    private fun selectSectorLevel(name: String) {
        _uiState.update { state ->
            state.copy(
                sectorLevelList = state.sectorLevelList.map {
                    it.copy(
                        isSelected = it.levelName == name
                    )
                },
                selectedSectorLevel = state.sectorLevelList.filter {
                    it.levelName == name
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
                            SelectedSector(it.sectorId, it.sectorName, it.levelName, it.levelColor, it.sectorImg)
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
    }

    fun applySectorFilter(){
        viewModelScope.launch {
            _event.emit(SelectSectorBottomSheetEvent.ApplyFilter(
                uiState.value.selectedSector
            )
            )
        }
    }
}