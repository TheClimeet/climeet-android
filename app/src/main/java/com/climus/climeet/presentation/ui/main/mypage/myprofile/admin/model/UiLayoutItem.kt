package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

import com.climus.climeet.data.model.request.UpdateGymRouteVersionRequest

data class UiLayoutItem(
    val floor: Int,
    val gymImg: String
) {
    fun toLayoutRequestItem() = UpdateGymRouteVersionRequest.NewData.LayoutRequestItem(
        floor = floor,
        imgUrl = gymImg
    )
}
