package com.climus.climeet.presentation.ui.main.global.selectsector.model

data class SectorNameUiData(
    val name: String = "",
    var isSelected: Boolean = false,
    val floor: Int = 0,
    val onClickListener: (String) -> Unit
)
