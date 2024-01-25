package com.climus.climeet.data.model.request

data class ManagerSignUpRequest(
    val backGroundImageUri: String,
    val email: String,
    val gymName: String,
    val isAllowAdNotification: Boolean,
    val isAllowCommentNotification: Boolean,
    val isAllowFollowNotification: Boolean,
    val isAllowLikeNotification: Boolean,
    val loginId: String,
    val name: String,
    val password: String,
    val phoneNumber: String,
    val provideServiceList: List<String>
)