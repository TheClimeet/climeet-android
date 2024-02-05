package com.climus.climeet.presentation.ui.main.shorts.model

data class WallNameUiData(
    val name: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (String) -> Unit
)