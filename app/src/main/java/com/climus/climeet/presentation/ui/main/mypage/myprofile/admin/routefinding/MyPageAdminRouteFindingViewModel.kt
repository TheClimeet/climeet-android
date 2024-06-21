package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import androidx.core.graphics.toColorInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.R
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiHoldItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteChipData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.LevelColorData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.model.RouteColor
import com.climus.climeet.presentation.util.Constants
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
    val selectedLevelColorHex: String = ""
)

sealed class CreateRouteEvent {
    data object CreateRoute : CreateRouteEvent()
    data class ChangeSector(val img: String) : CreateRouteEvent()
}

data class MyPageAdminRouteFindingUiState(
    val levelList: List<UiLevelItem> = emptyList(),
    val sectorList: List<UiSectorItem> = emptyList(),
    val holdList: List<UiHoldItem> = emptyList(),
    val chipList: List<UiRouteChipData> = emptyList(),
)

sealed class MyPageAdminRouteFindingEvent {
    data object ShowDatePicker : MyPageAdminRouteFindingEvent()
    data object ShowSetLevel : MyPageAdminRouteFindingEvent()
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
                ),
                levelList = listOf(
                    UiLevelItem(
                        "하양", "#FFFFFF", "V1", ::setLevelColor
                    ), UiLevelItem(
                        "빨강", "#F34040", "V1", ::setLevelColor
                    ), UiLevelItem(
                        "주황", "#FF9000", "V1", ::setLevelColor
                    ), UiLevelItem(
                        "노랑", "#FDDA16", "V1", ::setLevelColor
                    ), UiLevelItem(
                        "초록", "#63B75D", "V1", ::setLevelColor
                    )
                ),
                sectorList = listOf(
                    UiSectorItem("PEEK1", Constants.TEST_IMG, true, ::setSector),
                    UiSectorItem("PEEK2", Constants.TEST_IMG, false, ::setSector),
                    UiSectorItem("PEEK3", Constants.TEST_IMG, false, ::setSector),
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
        viewModelScope.launch {
            _createRouteEvent.emit(CreateRouteEvent.ChangeSector(imgUrl))
        }
    }

    fun chipImageToUrl(file: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadFile(file).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                chipList = uiState.value.chipList + UiRouteChipData(
                                    createRouteUiState.value.selectedLevelText,
                                    createRouteUiState.value.selectedLevelColorHex,
                                    it.body.imgUrl,
                                    createRouteUiState.value.selectedHoldImage
                                )
                            )
                        }
                    }

                    is BaseState.Error -> {

                    }
                }

            }
        }
    }

    // --end createRoute

    private val initDate = LocalDate.now()
    val selectedDateText =
        MutableStateFlow("${initDate.year}년 ${initDate.monthValue}월 ${initDate.dayOfMonth}일 (${dayOfWeekMap[initDate.dayOfWeek]})")
    val selectedDate = MutableStateFlow(initDate)

    val selectedLevel = MutableStateFlow(
        UiLevelItem(
            climeetLevel = "레벨 설정",
            colorHex = "#FFFFFF",
            colorName = "-",
            setLevelListener = ::empty
        )
    )

    val colorList = LevelColorData.COLORS
    val isCompletable = MutableLiveData(true)

    fun setSelectedDate(updateDate: LocalDate) {
        selectedDate.update { updateDate }
        selectedDateText.update {
            "${updateDate.year}년 ${updateDate.monthValue}월 ${updateDate.dayOfMonth}일 (${dayOfWeekMap[updateDate.dayOfWeek]})"
        }
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

    fun addLevelColor() {

        _uiState.update { state ->
            state.copy(
                levelList = uiState.value.levelList + selectedLevel.value
            )
        }

    }

    fun resetSelectedColorAndLevel() {
        selectedLevel.update {
            it.copy(
                climeetLevel = "레벨 설정",
                colorHex = "#FFFFFF",
                colorName = "-",
                setLevelListener = ::empty
            )
        }
    }

    fun isColorAlreadySelected(): Boolean {
        val isComplete = !_uiState.value.levelList.any { it.colorName == selectedLevel.value.colorName }
        isCompletable.value = isComplete
        return isComplete
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
    }

}