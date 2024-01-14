package com.climus.climeet.presentation.ui.intro.signup.climer.model

data class FollowCrag(
    var profileUrl : String? = null,
    var name : String = "",
    var followers : Int = 0,
    var keyword: String = "",
    var isFollowing: Boolean = false
)
