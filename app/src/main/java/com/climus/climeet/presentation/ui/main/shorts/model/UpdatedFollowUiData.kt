package com.climus.climeet.presentation.ui.main.shorts.model

data class UpdatedFollowUiData(
    val userId: Long = 0,
    val gymId: Long = 0,
    val profileImg: String? = "",
    val name: String = "",
    val viewType: Int= 0,
    val onClickListener: (Long, Long, Boolean) -> Unit,
    val isGym: Boolean = false,
    val navigateToAddFollow: () -> Unit
)

