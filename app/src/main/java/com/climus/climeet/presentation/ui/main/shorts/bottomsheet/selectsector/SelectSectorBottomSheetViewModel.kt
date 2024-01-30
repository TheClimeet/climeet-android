package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.selectsector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SelectSectorBottomSheetUiState(
    val isOneFloor: Boolean = false,
    val curFloor: Int = 1,
    val sectorImg: String = "",
    val sectorNameList: List<String> = emptyList(),
    val sectorLevelList: List<SectorLevelUiData> = emptyList(),
    val sectorImgList: List<SectorImageUiData> = emptyList()
)

data class SectorLevelUiData(
    val levelName: String = "",
    val levelColor: String = ""
)

data class SectorImageUiData(
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = ""
)

sealed class SelectSectorBottomSheetEvent{
    data object NavigateToBack: SelectSectorBottomSheetEvent()
}

@HiltViewModel
class SelectSectorBottomSheetViewModel @Inject constructor(

): ViewModel() {

    private val _uiState = MutableStateFlow(SelectSectorBottomSheetUiState())
    val uiState: StateFlow<SelectSectorBottomSheetUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SelectSectorBottomSheetEvent>()
    val event: SharedFlow<SelectSectorBottomSheetEvent> = _event.asSharedFlow()

    var cragId: Long = 0
    var cragName: String = ""

    fun setCragInfo(id: Long, name: String){
        cragId = id
        cragName = name
        getCragInfo(cragId)
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(SelectSectorBottomSheetEvent.NavigateToBack)
        }
    }

    private fun getCragInfo(id: Long){

        viewModelScope.launch {
            // todo 암장 정보 가져오기


        }
    }
}