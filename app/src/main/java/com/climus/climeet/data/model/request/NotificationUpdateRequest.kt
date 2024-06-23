package com.climus.climeet.data.model.request

data class NotificationUpdateRequest(
    val isAllowFollowNotification: Boolean,
    val isAllowLikeNotification: Boolean,
    val isAllowCommentNotification: Boolean,
    val isAllowAdNotification: Boolean,
)
