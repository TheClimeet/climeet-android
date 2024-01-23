package com.climus.climeet.data.model.request

data class ClimerSignupRequest(
    val nickName: String,
    val climbingLevel: ClimbingLevel,
    val discoveryChannel: DiscoveryChannel,
    val profileImgUrl: String?,
    val gymFollowList: List<String>,
    val isAllowFollowNotification: Boolean,
    val isAllowLikeNotification: Boolean,
    val isAllowCommentNotification: Boolean,
    val isAllowAdNotification: Boolean
)

enum class ClimbingLevel { BEGINNER, NOVICE, INTERMEDIATE, ADVANCED, EXPERT }
enum class DiscoveryChannel { INSTAGRAM_FACEBOOK, YOUTUBE, FRIEND_RECOMMENDATION, BLOG_CAFE_COMMUNITY, OTHER }
