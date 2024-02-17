package com.climus.climeet.presentation.ui.main.home.searchcrag.model

data class FollowClimber(

    val userId: Long,
    val climberName: String = "",
    val profileImageUrl : String = "",
    val keyword: String = "",
    var followerCount : Long = 0,
    var isFollowing: Boolean = false

)
