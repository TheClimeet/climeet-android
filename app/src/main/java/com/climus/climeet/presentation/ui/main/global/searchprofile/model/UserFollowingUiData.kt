package com.climus.climeet.presentation.ui.main.global.searchprofile.model

data class UserFollowingUiData(
    val id: Long,
    val imgUrl: String = "",
    val name : String = "",
    var followers : Int = 0,
    var isFollowing: Boolean,
    val navigateToProfile : (Long) -> Unit,
    val follow : (Long) -> Unit,
    val unFollow: (Long) -> Unit
)
