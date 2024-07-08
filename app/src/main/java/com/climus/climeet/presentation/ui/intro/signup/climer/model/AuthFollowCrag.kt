package com.climus.climeet.presentation.ui.intro.signup.climer.model

data class AuthFollowCrag(
    val id: Long,
    val imgUrl: String = "",
    val name : String = "",
    val keyword: String = "",
    var followers : Int = 0,
    var isFollowing: Boolean = false,
    val onFollowListener: (Long) -> Unit,
    val onFollowCancelListener: (Long) -> Unit
)
