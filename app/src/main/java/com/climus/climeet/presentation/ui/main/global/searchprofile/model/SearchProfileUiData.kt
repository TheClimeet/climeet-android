package com.climus.climeet.presentation.ui.main.global.searchprofile.model

data class SearchProfileUiData(
    val id: Long,
    val imgUrl: String = "",
    val name : String = "",
    val keyword: String = "",
    var followers : Int = 0,
    var isFollowing: Boolean = false,
    val navigateToProfile : (Long) -> Unit,
    val follow : (Long) -> Unit,
    val unFollow: (Long) -> Unit
)
