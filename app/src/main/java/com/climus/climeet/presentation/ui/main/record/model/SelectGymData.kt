package com.climus.climeet.presentation.ui.main.record.model

data class SelectGymData(
    var id : Long = -1,
    var name : String = "",
    val onClickListener: (SelectGymData) -> Unit
)
