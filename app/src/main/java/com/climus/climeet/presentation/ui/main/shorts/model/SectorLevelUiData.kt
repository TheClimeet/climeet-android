package com.climus.climeet.presentation.ui.main.shorts.model

data class SectorLevelUiData(
    val levelName: String = "",
    val levelColor: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (String) -> Unit
)