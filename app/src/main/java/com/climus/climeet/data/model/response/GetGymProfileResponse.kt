package com.climus.climeet.data.model.response

data class GetGymProfileResponse(
    val gymProfileImageUrl: String,
    val managerProfileImageUrl: String,
    val gymName: String,
    val followerCount: Int,
    val followingCount: Int,
    val averageRating: Float,
    val reviewCount: Int
)
