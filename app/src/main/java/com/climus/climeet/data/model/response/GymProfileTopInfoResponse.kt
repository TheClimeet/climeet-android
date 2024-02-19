package com.climus.climeet.data.model.response

data class GymProfileTopInfoResponse (
    val gymId: Long,
    val gymProfileImageUrl: String,
    val gymBackGroundImageUrl: String,
    val gymName: String,
    val followerCount: Int,
    val followingCount: Int,
    val averageRating: Float,
    val reviewCount: Int
)