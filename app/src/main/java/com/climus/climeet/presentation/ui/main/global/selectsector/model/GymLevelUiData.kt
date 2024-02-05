package com.climus.climeet.presentation.ui.main.global.selectsector.model

data class GymLevelUiData(
    val levelName: String = "",
    val levelColor: String = "",
    var isSelected: Boolean = false,
    val difficulty: Int = -1,
    val onClickListener: (GymLevelUiData) -> Unit
)