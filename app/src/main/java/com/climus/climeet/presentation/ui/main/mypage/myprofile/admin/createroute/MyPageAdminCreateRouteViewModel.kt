package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.createroute

import android.util.Log
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.climus.climeet.R
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiHoldItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteChipData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import com.climus.climeet.presentation.util.Constants
import com.climus.climeet.presentation.util.Constants.TAG
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
import javax.inject.Inject

data class CreateRouteUiState(
    val levelList: List<UiLevelItem> = emptyList(),
    val sectorList: List<UiSectorItem> = emptyList(),
    val holdList: List<UiHoldItem> = emptyList(),
    val chipList: List<UiRouteChipData> = emptyList(),
    val selectedHoldImage: Int = R.drawable.ic_black_hold,
    val selectedLevelText: String = "",
    val selectedLevelColor: Int = 0,
    val selectedLevelColorHex: String = ""
)

sealed class CreateRouteEvent {
    data object CreateRoute : CreateRouteEvent()
    data class ChangeSector(val img: String) : CreateRouteEvent()
}

@HiltViewModel
class MyPageAdminCreateRouteViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRouteUiState())
    val uiState: StateFlow<CreateRouteUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateRouteEvent>()
    val event: SharedFlow<CreateRouteEvent> = _event.asSharedFlow()

    fun createRoute() {
        viewModelScope.launch {
            _event.emit(CreateRouteEvent.CreateRoute)
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
                        "하양", "#FFFFFF", ::setLevelColor
                    ), UiLevelItem(
                        "빨강", "#F34040", ::setLevelColor
                    ), UiLevelItem(
                        "주황", "#FF9000", ::setLevelColor
                    ), UiLevelItem(
                        "노랑", "#FDDA16", ::setLevelColor
                    ), UiLevelItem(
                        "초록", "#63B75D", ::setLevelColor
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
        _uiState.update { state ->
            state.copy(
                selectedHoldImage = image
            )
        }
    }

    private fun setLevelColor(colorName: String, colorHex: String) {
        _uiState.update { state ->
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
            _event.emit(CreateRouteEvent.ChangeSector(imgUrl))
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
                                    uiState.value.selectedLevelText,
                                    uiState.value.selectedLevelColorHex,
                                    it.body.imgUrl,
                                    uiState.value.selectedHoldImage
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


}