package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import android.net.Uri
import android.util.Log
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.R
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiHoldItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLayoutItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteChipData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.LevelColorData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.MyPageAdminRouteData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.RouteColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject


data class CreateRouteUiState(
    val selectedHoldImage: Int = R.drawable.ic_black_hold,
    val selectedLevelText: String = "",
    val selectedLevelColor: Int = 0,
    val selectedLevelColorHex: String = "",
    val selectedSectorName: String = ""
)

sealed class CreateRouteEvent {
    data object CreateRoute : CreateRouteEvent()
    data class ChangeSector(val img: String) : CreateRouteEvent()
}

data class MyPageAdminRouteFindingUiState(
    val levelList: List<UiLevelItem> = emptyList(),
    val layoutList: List<UiLayoutItem> = listOf(
        UiLayoutItem(1, ""), UiLayoutItem(2, "")
    ),
    val sectorList: List<UiSectorItem> = emptyList(),
    val holdList: List<UiHoldItem> = emptyList(),
    val chipList: List<UiRouteChipData> = emptyList(),
    val routeList: List<UiRouteItem> = emptyList()
)

sealed class MyPageAdminRouteFindingEvent {
    data object ShowDatePicker : MyPageAdminRouteFindingEvent()
    data object ShowSetLevel : MyPageAdminRouteFindingEvent()
    data object GoToGallery : MyPageAdminRouteFindingEvent()
    data object GoToCreateRoute : MyPageAdminRouteFindingEvent()
    data object NavigateToBack : MyPageAdminRouteFindingEvent()
    data object DeleteSecondFloor : MyPageAdminRouteFindingEvent()
    data class ShowLayoutImg(val uri: Uri) : MyPageAdminRouteFindingEvent()
}

@HiltViewModel
class MyPageAdminRouteFindingViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    fun empty(t1: String, t2: String) {}

    private val _event = MutableSharedFlow<MyPageAdminRouteFindingEvent>()
    val event: SharedFlow<MyPageAdminRouteFindingEvent> = _event.asSharedFlow()

    private val _uiState = MutableStateFlow(MyPageAdminRouteFindingUiState())
    val uiState: StateFlow<MyPageAdminRouteFindingUiState> = _uiState.asStateFlow()

    // createRoute

    private val _createRouteUiState = MutableStateFlow(CreateRouteUiState())
    val createRouteUiState: StateFlow<CreateRouteUiState> = _createRouteUiState.asStateFlow()

    private val _createRouteEvent = MutableSharedFlow<CreateRouteEvent>()
    val createRouteEvent: SharedFlow<CreateRouteEvent> = _createRouteEvent.asSharedFlow()

    fun createRoute() {
        viewModelScope.launch {
            _createRouteEvent.emit(CreateRouteEvent.CreateRoute)
        }
    }

    fun setData() {
        _uiState.update { state ->
            state.copy(
                holdList = listOf(
                    UiHoldItem(R.drawable.ic_white_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_red_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_orange_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_yellow_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_green_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_skyblue_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_blue_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_indigo_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_purple_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_brown_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_grey_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_pink_hold, ::setHoldImage),
                    UiHoldItem(R.drawable.ic_black_hold, ::setHoldImage),
                )
            )
        }
    }


    private fun setHoldImage(image: Int) {
        _createRouteUiState.update { state ->
            state.copy(
                selectedHoldImage = image
            )
        }
    }

    private fun setLevelColor(colorName: String, colorHex: String) {
        _createRouteUiState.update { state ->
            state.copy(
                selectedLevelColor = colorHex.toColorInt(),
                selectedLevelText = colorName,
                selectedLevelColorHex = colorHex
            )
        }
    }

    private fun setSector(name: String, imgUrl: String) {
        _uiState.update { state ->
            state.copy(
                sectorList = uiState.value.sectorList.map { data ->
                    if (name == data.sectorName) {
                        data.copy(isSelected = !data.isSelected)
                    } else {
                        data.copy(isSelected = false)
                    }
                }
            )
        }

        _createRouteUiState.update { state ->
            state.copy(
                selectedSectorName = name
            )
        }
        viewModelScope.launch {
            _createRouteEvent.emit(CreateRouteEvent.ChangeSector(imgUrl))
        }
    }

    fun chipImageToUrl(file: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadFile(file).let {
                when (it) {
                    is BaseState.Success -> {
                        val selectedChipData = UiRouteChipData(
                            createRouteUiState.value.selectedSectorName,
                            createRouteUiState.value.selectedLevelText,
                            createRouteUiState.value.selectedLevelColorHex,
                            it.body.imgUrl,
                            createRouteUiState.value.selectedHoldImage
                        )

                        setRoute(selectedChipData)
                    }

                    is BaseState.Error -> {

                    }
                }

            }
        }
    }

    private fun setRoute(selectedChipData: UiRouteChipData) {
        _uiState.update { state ->
            val existingRouteItem =
                state.routeList.find { it.sectorName == selectedChipData.sectorName }

            val updatedRouteList = if (existingRouteItem != null) {
                state.routeList.map { item ->
                    if (item.sectorName == selectedChipData.sectorName) {
                        item.copy(chipList = item.chipList + selectedChipData)
                    } else {
                        item
                    }
                }
            } else {
                state.routeList + UiRouteItem(selectedChipData.sectorName, listOf(selectedChipData))
            }

            state.copy(
                chipList = state.chipList + selectedChipData,
                routeList = updatedRouteList
            )
        }
    }

    fun deleteRoute(deletingChipData: UiRouteChipData) {
        Log.d("tlqkf", "호출 : ${deletingChipData}")
        _uiState.update { state ->
            val updatedChipList = state.chipList.filter { it != deletingChipData }

            val updatedRouteList = state.routeList.mapNotNull { routeItem ->
                if (routeItem.sectorName == deletingChipData.sectorName) {
                    val updatedChipListForSector =
                        routeItem.chipList.filter { it != deletingChipData }

                    if (updatedChipListForSector.isNotEmpty()) {
                        routeItem.copy(chipList = updatedChipListForSector)
                    } else {
                        null
                    }
                } else {
                    routeItem
                }
            }

            state.copy(
                chipList = updatedChipList,
                routeList = updatedRouteList
            )
        }
        Log.d("tlqkf", "호출 후 : ${uiState.value.chipList}\n ${uiState.value.routeList}")

    }

    // --end createRoute

    private val initDate = MyPageAdminRouteData.selectedDate
    val selectedDateText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일 (${dayOfWeekMap[initDate.dayOfWeek]})")
    val selectedDate = MutableStateFlow(initDate)
    private val defaultLevelItem = UiLevelItem(
        climeetLevel = "레벨 설정",
        colorHex = "#FFFFFF",
        colorName = "-",
        setLevelListener = ::setLevelColor
    )

    val selectedLevel = MutableStateFlow(defaultLevelItem)
    val modifyingLevel = MutableStateFlow(defaultLevelItem)

    val colorList = LevelColorData.COLORS
    val isCompletable = MutableLiveData(true)
    val isLevelAdd = MutableLiveData(true)

    val selectedLayoutFloor = MutableStateFlow(1)
    val isSecondFloorExist = MutableLiveData(false)

    val selectedSectorFloor = MutableStateFlow(1)
    private val _floorSectorList = MutableStateFlow(listOf<UiSectorItem>())
    val floorSectorList: StateFlow<List<UiSectorItem>> = _floorSectorList

    val defaultSectorItem = UiSectorItem("", "", 1, false, ::setSector)
    val selectedSector = MutableStateFlow(defaultSectorItem)
    val selectedImageType = MutableStateFlow(DataType.GYM)
    val modifyingSector = MutableStateFlow(defaultSectorItem)

    fun getRouteFindingData() {
        viewModelScope.launch {
            repository.getGymRouteFindingData(selectedDate.value.toString()).let {
                when (it) {
                    is BaseState.Success -> {
                        val result = it.body
                        if (result.maxFloor == 2) {
                            isSecondFloorExist.postValue(true)
                        }

                        val chipDataList: List<UiRouteChipData> =
                            result.routeList?.map { data -> data.toUiRouteChipData() } ?: emptyList()
                        val routeItemList = groupBySectorName(chipDataList)

                        _uiState.update { state ->
                            val updatedLayoutList: List<UiLayoutItem> =
                                result.layoutList?.map { data -> data.toUiLayoutItem() }
                                    ?: emptyList()
                            val completeLayoutList = listOf(
                                UiLayoutItem(
                                    floor = 1,
                                    gymImg = updatedLayoutList.find { it.floor == 1 }?.gymImg ?: ""
                                ),
                                UiLayoutItem(
                                    floor = 2,
                                    gymImg = updatedLayoutList.find { it.floor == 2 }?.gymImg ?: ""
                                )
                            )

                            state.copy(
                                levelList = result.difficultyList?.map { data ->
                                    data.toUiLevelItem(
                                        ::setLevelColor
                                    )
                                } ?: emptyList(),
                                layoutList = completeLayoutList,
                                sectorList = result.sectorList?.map { data -> data.toUiSectorItem(::setSector) }
                                    ?: emptyList(),
                                chipList = chipDataList,
                                routeList = routeItemList
                            )
                        }
                        _floorSectorList.update { uiState.value.sectorList.filter { it.sectorFloor == 1 } }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    private fun groupBySectorName(chipList: List<UiRouteChipData>): List<UiRouteItem> {
        val groupedMap = chipList.groupBy { it.sectorName }

        return groupedMap.map { (sectorName, chips) ->
            UiRouteItem(sectorName, chips)
        }
    }

    fun setSelectedDate(updateDate: LocalDate) {
        selectedDate.update { updateDate }
        selectedDateText.update {
            "${updateDate.year}년 ${updateDate.monthValue}월 ${updateDate.dayOfMonth}일 (${dayOfWeekMap[updateDate.dayOfWeek]})"
        }
        getRouteFindingData()
    }

    fun selectColor(color: RouteColor) {
        selectedLevel.update {
            it.copy(
                colorHex = color.color,
                colorName = color.name
            )
        }
    }

    fun selectLevel(level: String) {
        selectedLevel.update {
            it.copy(
                climeetLevel = level
            )
        }
    }

    fun updateIsLevelAdd(isAdd: Boolean) {
        isLevelAdd.postValue(isAdd)
    }

    fun addLevelColor() {
        modifyingLevel.update {
            selectedLevel.value
        }
        _uiState.update { state ->
            val updatedList = state.levelList + selectedLevel.value
            val sortedList = updatedList.filter { it.climeetLevel != "C" }
                .sortedBy { LevelColorData.LEVELS.indexOf(it.climeetLevel) } +
                    updatedList.filter { it.climeetLevel == "C" }

            state.copy(
                levelList = sortedList
            )
        }
        updateIsLevelAdd(false)
    }

    fun modifyLevel() {
        _uiState.update { state ->
            val updatedList = state.levelList.map { level ->
                if (level.colorHex == modifyingLevel.value.colorHex) {
                    selectedLevel.value
                } else {
                    level
                }
            }
            updateModifingLevel(selectedLevel.value)
            state.copy(levelList = updatedList)
        }
    }

    fun deleteLevel() {
        _uiState.update { state ->
            val updatedList = state.levelList.filterNot { level ->
                level.colorHex == modifyingLevel.value.colorHex
            }
            state.copy(levelList = updatedList)
        }
        resetSelectedColorAndLevel()
    }

    fun updateModifingLevel(item: UiLevelItem) {
        modifyingLevel.update { item }
    }

    fun resetSelectedColorAndLevel() {
        selectedLevel.update { defaultLevelItem }
        updateModifingLevel(defaultLevelItem)
        updateIsLevelAdd(true)
    }

    fun isColorAlreadySelected(): Boolean {
        val isComplete = !_uiState.value.levelList.any {
            it.colorName == selectedLevel.value.colorName
        } || modifyingLevel.value.colorName == selectedLevel.value.colorName
        isCompletable.value = isComplete
        return isComplete
    }

    fun updateImg(uri: String) {
        if (selectedImageType.value == DataType.GYM) {
            _uiState.update { state ->
                val updatedLayoutList = state.layoutList.toMutableList()
                val selectedFloor = selectedLayoutFloor.value
                updatedLayoutList[selectedFloor - 1] =
                    updatedLayoutList[selectedFloor - 1].copy(gymImg = uri)

                state.copy(
                    layoutList = updatedLayoutList
                )
            }
        } else {
            selectedSector.update {
                it.copy(
                    sectorImg = uri
                )
            }
        }

    }

    fun selectFloor(floor: Int) {
        selectedImageType.value = DataType.GYM
        selectedLayoutFloor.update { floor }
        val uri = uiState.value.layoutList[floor - 1].gymImg.toUri()

        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.ShowLayoutImg(uri))
        }
    }

    fun addSecondFloor() {
        isSecondFloorExist.postValue(true)
        selectFloor(2)
    }

    fun deleteSecondFloor() {
        selectedImageType.value = DataType.GYM
        isSecondFloorExist.postValue(false)
        updateImg("")
        selectFloor(1)
        _uiState.update { state ->
            state.copy(
                sectorList = state.sectorList.filter { it.sectorFloor == 1 }
            )
        }
        if (selectedSectorFloor.value == 2) {
            selectedSectorFloor.value = 1
            _floorSectorList.update { uiState.value.sectorList }
            viewModelScope.launch {
                _event.emit(MyPageAdminRouteFindingEvent.DeleteSecondFloor)
            }
        }
    }

    fun addSector() {
        _uiState.update { state ->
            val updatedList = state.sectorList + selectedSector.value
            state.copy(
                sectorList = updatedList
            )
        }
        _floorSectorList.update { it + selectedSector.value }
        selectedSector.update { defaultSectorItem }
    }

    fun deleteSector(deleteItem: UiSectorItem) {
        _uiState.update { state ->
            state.copy(
                sectorList = state.sectorList.filterNot { it == deleteItem }
            )
        }
        _floorSectorList.update { uiState.value.sectorList.filter { it.sectorFloor == selectedSectorFloor.value } }
    }

    fun modifySector() {
        _uiState.update { state ->
            val updatedList = state.sectorList.map { sector ->
                if (sector.sectorName == modifyingSector.value.sectorName) {
                    selectedSector.value
                } else {
                    sector
                }
            }
            state.copy(sectorList = updatedList)
        }
        _floorSectorList.update {
            it.map { sector ->
                if (sector.sectorName == modifyingSector.value.sectorName) {
                    selectedSector.value
                } else {
                    sector
                }
            }
        }
        resetSector()
    }

    fun changeFloorSector(floor: Int) {
        selectedSectorFloor.value = floor
        resetSector()
        _floorSectorList.update {
            uiState.value.sectorList.filter { it.sectorFloor == floor }
        }
    }

    fun resetSector() {
        selectedSector.update { defaultSectorItem }
        modifyingSector.update { defaultSectorItem }
    }

    fun noUse(dateDate: LocalDate) {}

    fun showDatePicker() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.ShowDatePicker)
        }
    }

    fun showSetLevel() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.ShowSetLevel)
        }
    }

    fun goToGallery(type: Int) {
        if (type == 0) {
            selectedImageType.value = DataType.GYM
        } else {
            selectedImageType.value = DataType.SECTOR
        }
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.GoToGallery)
        }
    }

    fun goToCreateRoute() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.GoToCreateRoute)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(MyPageAdminRouteFindingEvent.NavigateToBack)
        }
    }

    companion object {
        private val dayOfWeekMap = mapOf(
            DayOfWeek.MONDAY to "월",
            DayOfWeek.TUESDAY to "화",
            DayOfWeek.WEDNESDAY to "수",
            DayOfWeek.THURSDAY to "목",
            DayOfWeek.FRIDAY to "금",
            DayOfWeek.SATURDAY to "토",
            DayOfWeek.SUNDAY to "일"
        )
        private val colorOrder = listOf(
            "검정" to "#000000",
            "회색" to "#8B8B8B",
            "갈색" to "#6E4C41",
            "핑크" to "#FF74E9",
            "보라" to "#A259FF",
            "남색" to "#393FD6",
            "파랑" to "#0094FF",
            "하늘" to "#74D5FF",
            "초록" to "#63B75D",
            "노랑" to "#FDDA16",
            "주황" to "#FF9000",
            "빨강" to "#F34040",
            "하양" to "#FFFFFF",
            "컴피" to "#BEDF22"
        )
    }

}

enum class DataType {
    GYM, SECTOR
}