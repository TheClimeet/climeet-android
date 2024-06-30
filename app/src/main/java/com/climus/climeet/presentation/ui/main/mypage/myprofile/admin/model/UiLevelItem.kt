package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

import com.climus.climeet.data.model.request.UpdateGymRouteVersionRequest

data class UiLevelItem(
    val colorName: String = "",
    val colorHex: String = "",
    val climeetLevel: String = "",
    val setLevelListener: (String, String) -> Unit
) {
    fun toDifficultyRequestItem() = UpdateGymRouteVersionRequest.NewData.DifficultyRequestItem(
        gymDifficultyName = colorName,
        climeetDifficultyName = climeetLevel
    )
}

