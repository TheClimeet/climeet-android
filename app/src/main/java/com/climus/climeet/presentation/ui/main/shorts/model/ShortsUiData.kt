package com.climus.climeet.presentation.ui.main.shorts.model

data class ShortsUiData(
    val shortsId: Long = 0,
    val thumbnailImg: String = "",
    val gymId: Long = 0,
    val gymName: String = "",
    val climeetLevelColor: String = "",
    val videoUrl: String = "",
    val userId: Long = 0,
    val profileImgUrl: String = "",
    val userName: String = "",
    val description: String = "",
    val sectorId: Long = 0,
    val sectorName: String = "",
    val sectorImgUrl: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val bookMarkCount: Int = 0,
    val sharedCount: Int = 0,
    val isSoundEnabled: Boolean = false,
    val isLiked: Boolean = false,
    val isBookMarked: Boolean = false,
)
