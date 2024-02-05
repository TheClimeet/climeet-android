package com.climus.climeet.presentation.ui.main.record.model

data class RouteRecordUiData(
    val sectorId: Long = 0,
    val sectorName: String = "",
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = "",
    var challengeNum: Int = 0,
    var isClear: Boolean = false,
    var isSelected: Boolean = false,
    val onClickListener: (Long) -> Unit
)
