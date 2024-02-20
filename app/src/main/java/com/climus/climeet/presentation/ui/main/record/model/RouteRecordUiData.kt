package com.climus.climeet.presentation.ui.main.record.model

import com.climus.climeet.R

data class RouteRecordUiData(
    val routeId: Long = 0,
    val sectorId: Long = 0,
    val sectorName: String = "",
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = "",
    var challengeNum: Int = 0,
    var isClear: Boolean = false,
    var isSelected: Boolean = false,
    val holdImg: Int = R.drawable.ic_white_hold,
    val onClickListener: (Long) -> Unit
)
