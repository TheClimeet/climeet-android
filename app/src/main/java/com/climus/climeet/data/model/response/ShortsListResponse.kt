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
    val shortsId: Int,
    val thumbnailImageUrl: String
)

data class ShortsDetailInfo(
    val bookmarkCount: Int,
    val bookmarked: Boolean,
    val commentCount: Int,
    val description: String,
    val gymId: Int,
    val gymName: String,
    val isSoundEnabled: Boolean,
    val likeCount: Int,
    val liked: Boolean,
    val sectorId: Int,
    val sectorName: String,
    val shareCount: Int,
    val shortsId: Int,
    val videoUrl: String
)

