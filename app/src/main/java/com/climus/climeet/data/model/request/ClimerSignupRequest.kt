package com.climus.climeet.data.model.request

import com.google.gson.annotations.SerializedName

data class ClimerSignupRequest(
    @SerializedName("nickName")
    val nickName: String,

    @SerializedName("climbingLevel")
    val climbingLevel: ClimbingLevel,

    @SerializedName("discoveryChannel")
    val discoveryChannel: DiscoveryChannel,

    @SerializedName("profileImgUrl")
    val profileImgUrl: String?,

    @SerializedName("gymFollowList")
    val gymFollowList: List<String>,

    @SerializedName("isAllowFollowNotification")
    val isAllowFollowNotification: Boolean,

    @SerializedName("isAllowLikeNotification")
    val isAllowLikeNotification: Boolean,

    @SerializedName("isAllowCommentNotification")
    val isAllowCommentNotification: Boolean,

    @SerializedName("isAllowAdNotification")
    val isAllowAdNotification: Boolean
)

enum class ClimbingLevel { BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT }
enum class DiscoveryChannel { INSTAGRAM_FACEBOOK, YOUTUBE, FRIEND_RECOMMENDATION, BLOG_CAFE_COMMUNITY, OTHER }
