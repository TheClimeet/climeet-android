package com.climus.climeet.presentation.ui.main.record.model

import com.climus.climeet.data.model.response.ClimbedGym

data class SelectGymData(
    var id : Int = 0,
    var name : String = "",
    val onClickListener: (SelectGymData) -> Unit
)

fun ClimbedGym.toSelectGymData(
    onClickListener: (SelectGymData) -> Unit
) = SelectGymData(
    id = gymId,
    name = gymName,
    onClickListener = onClickListener
)


