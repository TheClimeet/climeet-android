package com.climus.climeet.presentation.ui.main.global.selectsector.model

data class RouteUiData(
    val routeId: Long = 0,
    val sectorId: Long = 0,
    val sectorName: String = "",
    val gymLevelName: String = "",
    val gymLevelColor: String = "",
    val climeetLevelName: String = "",
    val routeImg: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (Long) -> Unit
)