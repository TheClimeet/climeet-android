package com.climus.climeet.presentation.ui.main.global.selectsector.model

data class SelectedRoute(
    val routeId: Long = -1,
    val sectorId: Long = -1,
    val sectorName: String = "",
    val cragName: String = "",
    val difficulty: Int = 0,
    val gymLevelName: String = "",
    val gymLevelColor: String = "",
    val climeetLevelName: String = "",
    val routeImg: String = "",
)
