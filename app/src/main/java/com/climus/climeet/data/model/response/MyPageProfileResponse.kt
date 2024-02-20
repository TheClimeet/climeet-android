package com.climus.climeet.data.model.response

class MyPageProfileResponse(
    val userId: Long,
    val userName: String,
    val profileImgUrl: String,
    val followerCount: Int,
    val followingCount: Int,
    val isManager: Boolean
)