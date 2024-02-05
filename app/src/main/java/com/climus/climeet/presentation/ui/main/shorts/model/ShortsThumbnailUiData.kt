package com.climus.climeet.presentation.ui.main.shorts.model

data class ShortsThumbnailUiData(
    val shortsId: Long = 0,
    val thumbnailImg: String = "",
    val gymName: String = "",
    val originLevelColor: String = "",
    val climeetLevelColor: String = "",
    val onClickListener: (Long) -> Unit
)
