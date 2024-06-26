package com.climus.climeet.presentation.ui.main.mypage.follow.model

data class FollowUiData(
    val userId: Long,
    val userName: String = "",
    val profileImageUrl: String = "",
    var followerCount: Int = 0,
    var followingCount: Int = 0,
    var isFollowing: Boolean = false,
    val navigateToProfile: (Long) -> Unit,
    val follow: (Long) -> Unit,
    val unFollow: (Long) -> Unit,
)