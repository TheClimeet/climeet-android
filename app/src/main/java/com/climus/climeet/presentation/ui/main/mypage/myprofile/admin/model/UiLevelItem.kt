package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

data class UiLevelItem(
    val colorName: String = "",
    val colorHex: String = "",
    val climeetLevel: String = "",
    val setLevelListener: (String, String) -> Unit
)
