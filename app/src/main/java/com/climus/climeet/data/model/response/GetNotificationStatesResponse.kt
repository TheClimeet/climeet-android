package com.climus.climeet.data.model.response

data class GetNotificationStatesResponse(
    val isAllowFollowNotification: Boolean,
    val isAllowLikeNotification: Boolean,
    val isAllowCommentNotification: Boolean,
    val isAllowAdNotification: Boolean,
)