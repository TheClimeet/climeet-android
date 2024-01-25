package com.climus.climeet.presentation.ui.main.shorts.model

data class UpdatedFollowUiData(
    val userId: Long = 0,
    val profileImg: String = "",
    val name: String = "",
    val onClickListener: (Long) -> Unit
)
