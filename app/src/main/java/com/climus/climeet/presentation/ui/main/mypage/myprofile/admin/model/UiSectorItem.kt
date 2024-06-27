package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

data class UiSectorItem(
    val sectorName: String = "",
    val sectorImg: String = "",
    val sectorFloor: Int = 1,
    var isSelected: Boolean = false,
    val setSectorListener: (String, String) -> Unit
)
