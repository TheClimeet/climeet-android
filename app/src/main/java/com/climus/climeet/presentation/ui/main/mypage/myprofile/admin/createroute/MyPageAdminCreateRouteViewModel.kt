package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.createroute

import androidx.lifecycle.ViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiHoldItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CreateRouteUiState(
    val levelList: List<UiLevelItem> = emptyList(),
    val sectorList: List<UiSectorItem> = emptyList(),
    val holdList: List<UiHoldItem> = emptyList()
)

sealed class CreateRouteEvent {

}

@HiltViewModel
class MyPageAdminCreateRouteViewModel @Inject constructor() : ViewModel() {


}