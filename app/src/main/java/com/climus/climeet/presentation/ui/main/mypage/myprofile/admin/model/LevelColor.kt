package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

data class LevelColor(
    val color: RouteColor,
    val level: String
)

data class RouteColor(
    val name: String = "-",
    val color: String = "#FFFFFF"
)
