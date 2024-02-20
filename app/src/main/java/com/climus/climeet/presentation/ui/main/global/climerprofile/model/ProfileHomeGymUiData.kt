package com.climus.climeet.presentation.ui.main.global.climerprofile.model

data class ProfileHomeGymUiData(
    val gymId: Long = 0,
    val profileImg: String = "",
    val name: String = "",
    val followerString : String = "",
    val onClickListener: (Long) -> Unit
)
