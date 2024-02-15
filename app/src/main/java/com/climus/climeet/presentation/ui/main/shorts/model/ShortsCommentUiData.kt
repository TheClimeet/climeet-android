package com.climus.climeet.presentation.ui.main.shorts.model

data class ShortsCommentUiData(
    val commentId: Long = -1L,
    val nickName: String = "",
    val profileImageUrl: String = "",
    val content: String = "",
    val commentLikeStatus: String = "",
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val type: Int = 0,
    val parentCommentId: Long = -1,
    val childCommentCount: Int = 0,
    val createDate: String = "",
    val changeLikeStatus: (Long, Boolean, Boolean) -> Unit,
    val showMoreComment: (Long, Int, Int, Int) -> Unit,
    val addSubComment: (Long) -> Unit,
    val remainSubCommentCount: Int = -1,
    val isLastSubComment: Boolean = false,
    val subCommentPage: Int = 0,
)


