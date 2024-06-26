package com.climus.climeet.presentation.ui.main.mypage.myshorts.model

data class MyPageShortsCommentUiData(
    val commentId: Long = 0,
    val shortsId: Long = 0,
    val content: String = "",
    val profileImage: String? = "",
    val createdAt: String = ""
)
