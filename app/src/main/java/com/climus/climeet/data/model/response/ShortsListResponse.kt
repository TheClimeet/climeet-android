package com.climus.climeet.data.model.response

data class ShortsListResponse(
    val hasNext: Boolean,
    val page: Int,
    val result: List<ShortsItem>
)

data class ShortsItem(
    val difficulty: Int,
    val gymName: String,
    val shortsDetailInfo: ShortsDetailInfo,
    val shortsId: Long,
    val thumbnailImageUrl: String
)

data class ShortsDetailInfo(
    val bookmarkCount: Int,
    val bookmarked: Boolean,
    val commentCount: Int,
    val description: String,
    val gymId: Long,
    val gymName: String,
    val isSoundEnabled: Boolean,
    val likeCount: Int,
    val liked: Boolean,
    val sectorId: Long,
    val sectorName: String,
    val shareCount: Int,
    val shortsId: Long,
    val videoUrl: String
)

