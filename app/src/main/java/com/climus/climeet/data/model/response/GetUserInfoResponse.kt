package com.climus.climeet.data.model.response

data class GetUserInfoResponse(
    val followerCount: Int,
    val followingCount: Int,
    val isFollower: Boolean,
    val userId: Int,
    val userName: String,
    val userProfileUrl: String
)