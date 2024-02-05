package com.climus.climeet.presentation.ui.main.record.model

data class RecordLevelData(
    val levelName: String = "",
    val levelColor: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (String) -> Unit
)