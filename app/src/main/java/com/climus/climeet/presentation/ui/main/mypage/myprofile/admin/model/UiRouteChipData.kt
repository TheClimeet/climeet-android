package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

import com.climus.climeet.R
import com.climus.climeet.data.model.request.UpdateGymRouteVersionRequest
import com.climus.climeet.presentation.util.Constants

data class UiRouteChipData(
    val sectorName: String = "",
    val gymLevelName: String = "",
    val gymLevelColor: String = "",
    val routeImg: String = "",
    val holdImg: Int = 0,
) {
    fun toRouteRequestItem() = UpdateGymRouteVersionRequest.NewData.RouteRequestItem(
        sectorName = sectorName,
        gymDifficultyName = gymLevelName,
        holdColor = Constants.holdDrawable[holdImg] ?: "하양",
        imgUrl = routeImg
    )
}
