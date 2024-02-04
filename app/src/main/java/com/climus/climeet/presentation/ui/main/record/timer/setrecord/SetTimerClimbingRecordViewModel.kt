package com.climus.climeet.presentation.ui.main.record.timer.setrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.presentation.ui.main.record.model.RecordLevelData
import com.climus.climeet.presentation.ui.main.record.model.RecordSectorData
import com.climus.climeet.presentation.ui.main.record.model.RecordWallData
import com.climus.climeet.presentation.util.Constants.TEST_IMG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddRecordUiState(
    val isSingleFloor: Boolean = false,
    val backgroundImage: String = "",
    val wallNameList: List<RecordWallData> = emptyList(),
    val sectorLevelList: List<RecordLevelData> = emptyList(),
    val sectorImageList: List<RecordSectorData> = emptyList(),
    val selectedSectorName: RecordWallData = RecordWallData {},
    val selectedSectorLevel: RecordLevelData = RecordLevelData {},
    val selectedSector: SelectedSector = SelectedSector()
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
class SetTimerClimbingRecordViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(AddRecordUiState())
    val uiState: StateFlow<AddRecordUiState> = _uiState.asStateFlow()

    var cragId: Long = 0
    var cragName: String = ""

    fun setCragInfo(id: Long, name: String) {
        cragId = id
        cragName = name
        getCragInfo(cragId)
    }

    private fun getCragInfo(id: Long) {

        viewModelScope.launch {
            //todo 암장 정보 가져오기
            // - floor 1개인지 두개인지 도 적용

            _uiState.update { state ->
                state.copy(
                    isSingleFloor = false
                )
            }

            setSectorInfo()
        }
    }

    private fun setSectorInfo() {

        // todo floor 별 초기데이터 삽입
        // todo sectorImageList 는 인기순 10개 최초로 받아오기

        _uiState.update { state ->
            state.copy(
                wallNameList = listOf(
                    RecordWallData("Cheesegrater", onClickListener = ::selectWallName),
                    RecordWallData("Jaws", onClickListener = ::selectWallName),
                    RecordWallData("The Wallus", onClickListener = ::selectWallName),
                ),
                sectorLevelList = listOf(
                    RecordLevelData("VB", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    RecordLevelData("V1", "#FFFFFF", onClickListener = ::selectSectorLevel),
                    RecordLevelData("V2", "#DDDDDD", onClickListener = ::selectSectorLevel),
                    RecordLevelData("V3", "#CCCCCC", onClickListener = ::selectSectorLevel),
                    RecordLevelData("V4", "#BBBBBB", onClickListener = ::selectSectorLevel),
                    RecordLevelData("V5", "#EEEEEE", onClickListener = ::selectSectorLevel),
                    RecordLevelData("V6", "#555555", onClickListener = ::selectSectorLevel),
                ),
                sectorImageList = listOf(
                    RecordSectorData(
                        0,
                        sectorName = "SECTOR 2-2",
                        levelName = "VB",
                        levelColor = "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RecordSectorData(
                        1,
                        sectorName = "SECTOR 2-2",
                        levelName = "V2",
                        levelColor = "#456213",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RecordSectorData(
                        2,
                        sectorName = "SECTOR 2-2",
                        levelName = "V3",
                        levelColor = "#BBBBBB",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RecordSectorData(
                        3,
                        sectorName = "SECTOR 2-2",
                        levelName = "V4",
                        levelColor = "#456213",
                        sectorImg = TEST_IMG,
                        onClickListener = ::selectSectorImage
                    ),
                    RecordSectorData(
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
                            SelectedSector(it.sectorId, it.sectorName, cragName, it.levelName, it.levelColor, it.sectorImg)
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
}
