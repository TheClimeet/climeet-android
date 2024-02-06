package com.climus.climeet.data.model.response

data class ShortsUpdatedFollowResponse(
    val followingUserId: Long,
    val followingUserName: String,
    val followingUserProfileUrl: String?="",
    val isUploadRecent: Boolean?=false
)
