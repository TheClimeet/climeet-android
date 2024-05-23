package com.climus.climeet.presentation.ui.main.global.searchprofile.model

import com.google.gson.annotations.SerializedName

data class UserFollowerUiData(
    val id: Long,
    val imgUrl: String = "",
    val name : String = "",
    var followers : Int = 0,
    var followings : Int = 0,
    var isFollowing: Boolean,
    val navigateToProfile : (Long) -> Unit,
    val follow : (Long) -> Unit,
    val unFollow: (Long) -> Unit
)

