package com.climus.climeet.presentation.ui.main.global.selectsector.model

import com.climus.climeet.R

data class RouteUiData(
    val routeId: Long = -1,
    val sectorId: Long = -1,
    val sectorName: String = "",
    val difficulty: Int = 0,
    val gymLevelName: String = "",
    val gymLevelColor: String = "",
    val climeetLevelName: String = "",
    val routeImg: String = "",
    var isSelected: Boolean = false,
    var challengeNum: Int = 0,
    var clearBtnState: Boolean = false,
    val holdImg: Int = R.drawable.ic_white_hold,
    val onClickListener: (RouteUiData) -> Unit
)