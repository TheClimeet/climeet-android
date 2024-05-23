package com.climus.climeet.data.model.response

data class GetAnnouncementResponse (
    val boardId: Long,
    val createdAt: String,
    val likeCount: Int,
    val title: String,
    val content: String,
    val profileImageUrl: String?,
    val image: String?
)