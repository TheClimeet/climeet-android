package com.climus.climeet.data.model.response

data class GetAnnouncementDetailResponse(
    val boardId: Long,
    val title: String,
    val createdAt: String,
    val profileImageUrl: String,
    val profileName: String,
    val followerCount: Int,
    val followingCount: Int,
    val content: String,
    val likeCount: Int,
    val imageList: List<String>?
)