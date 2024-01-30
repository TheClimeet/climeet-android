package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.shorts.model.SectorImageUiData
import com.climus.climeet.presentation.ui.main.shorts.model.SectorLevelUiData
import com.climus.climeet.presentation.ui.main.shorts.model.SectorNameUiData
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
    val sectorNameList: List<SectorNameUiData> = emptyList(),
    val sectorLevelList: List<SectorLevelUiData> = emptyList(),
    val sectorImageList: List<SectorImageUiData> = emptyList(),
)

sealed class FloorBtnState {
    data object FloorSelected : FloorBtnState()
    data object FloorUnSelected : FloorBtnState()
}

sealed class SelectSectorBottomSheetEvent {
    data object NavigateToBack : SelectSectorBottomSheetEvent()
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

        _uiState.update { state ->
            state.copy(
                sectorNameList = listOf(
                    SectorNameUiData("Cheesegrater", onClickListener = ::selectName),
                    SectorNameUiData("Jaws", onClickListener = ::selectName),
                    SectorNameUiData("The Wallus", onClickListener = ::selectName),
                ),
                sectorLevelList = listOf(
                    SectorLevelUiData("VB", "#BBBBBB", onClickListener = ::selectLevel),
                    SectorLevelUiData("V1", "#FFFFFF", onClickListener = ::selectLevel),
                    SectorLevelUiData("V2", "#DDDDDD", onClickListener = ::selectLevel),
                    SectorLevelUiData("V3", "#CCCCCC", onClickListener = ::selectLevel),
                    SectorLevelUiData("V4", "#BBBBBB", onClickListener = ::selectLevel),
                    SectorLevelUiData("V5", "#EEEEEE", onClickListener = ::selectLevel),
                    SectorLevelUiData("V6", "#555555", onClickListener = ::selectLevel),
                ),
                sectorImageList = listOf(
                    SectorImageUiData(
                        0,
                        "VB",
                        "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectImage
                    ),
                    SectorImageUiData(
                        1,
                        "VB",
                        "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectImage
                    ),
                    SectorImageUiData(
                        2,
                        "VB",
                        "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectImage
                    ),
                    SectorImageUiData(
                        3,
                        "VB",
                        "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectImage
                    ),
                    SectorImageUiData(
                        4,
                        "VB",
                        "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectImage
                    )
                )
            )
        }
    }

    private fun selectName(name: String) {
        _uiState.update { state ->
            state.copy(
                sectorNameList = state.sectorNameList.map {
                    it.copy(
                        isSelected = it.name == name
                    )
                }
            )
        }
    }

    private fun selectLevel(name: String) {
        _uiState.update { state ->
            state.copy(
                sectorLevelList = state.sectorLevelList.map {
                    it.copy(
                        isSelected = it.levelName == name
                    )
                }
            )
        }
    }

    private fun selectImage(id: Long) {
        _uiState.update { state ->
            state.copy(
                sectorImageList = state.sectorImageList.map {
                    it.copy(
                        isSelected = it.sectorId == id
                    )
                },
            )
        }
    }
}