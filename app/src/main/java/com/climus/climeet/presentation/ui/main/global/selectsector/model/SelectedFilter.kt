package com.climus.climeet.presentation.ui.main.global.selectsector.model

data class SelectedFilter(
    val cragId: Long = -1,
    val routeId: Long = -1,
    val sectorId: Long = -1,
    val sectorName: String = "",
    val cragName: String = "",
    val difficulty: Int = -1,
    val gymLevelName: String = "",
    val gymLevelColor: String = "",
    val climeetLevelName: String = "",
    val routeImg: String = "",
)
