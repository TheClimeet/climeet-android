package com.climus.climeet.presentation.ui.main.record.model

data class RecordSectorData(
    val sectorId: Long = 0,
    val sectorName: String = "",
    val levelName: String = "",
    val levelColor: String = "",
    val sectorImg: String = "",
    var isSelected: Boolean = false,
    val onClickListener: (Long) -> Unit
)