package com.climus.climeet.presentation.ui.main.global.selectsector.model

data class SectorNameUiData(
    val sectorId: Long = -1,
    val name: String = "",
    var isSelected: Boolean = false,
    val floor: Int = 0,
    val onClickListener: (SectorNameUiData) -> Unit
)
