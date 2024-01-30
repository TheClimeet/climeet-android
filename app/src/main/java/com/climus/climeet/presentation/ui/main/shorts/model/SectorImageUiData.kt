package com.climus.climeet.presentation.ui.main.shorts.model

data class SectorImageUiData(
    val sectorId: Long = 0,
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (Long) -> Unit
)