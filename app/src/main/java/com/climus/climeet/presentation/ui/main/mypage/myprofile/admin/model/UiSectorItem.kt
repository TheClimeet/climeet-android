package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

import com.climus.climeet.data.model.request.UpdateGymRouteVersionRequest

data class UiSectorItem(
    val sectorName: String = "",
    val sectorImg: String = "",
    val sectorFloor: Int = 1,
    var isSelected: Boolean = false,
    val setSectorListener: (String, String) -> Unit
) {
    fun toSectorRequestItem() = UpdateGymRouteVersionRequest.NewData.SectorRequestItem(
        name = sectorName,
        floor = sectorFloor,
        imgUrl = sectorImg
    )
}
