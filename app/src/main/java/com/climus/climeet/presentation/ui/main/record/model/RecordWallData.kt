package com.climus.climeet.presentation.ui.main.record.model

data class RecordWallData (
    val name: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (String) -> Unit
)