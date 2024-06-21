package com.climus.climeet.presentation.ui.main.mypage.announce.model

data class AnnouncementUiData (
    val boardId: Long = 0,
    val createdAt: String = "",
    val likeCount: Int = 0,
    val title: String = "",
    val content: String = "",
    val profileImageUrl: String? = null,
    val image: String? = null
)