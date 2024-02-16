package com.climus.climeet.data.model.response

data class ShortsSubCommentResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<ShortsSubCommentItem>
)

data class ShortsSubCommentItem(
    val commentId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val commentLikeStatus: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val parentCommentId: Long,
    val createdDate: String
)
