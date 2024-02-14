package com.climus.climeet.data.model.response

data class ShortsMainCommentResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<ShortsMainCommentItem>
)

data class ShortsMainCommentItem(
    val commentId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val commentLikeStatus: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val isParent: Boolean,
    val parentCommentId: Long,
    val childCommentCount: Int,
    val createdDate: String
)
