package com.climus.climeet.presentation.ui.main.record.model

import com.climus.climeet.data.model.response.UserClimbedGym

data class SelectGymData(
    var id : Int = 0,
    var name : String = "",
    val onClickListener: (SelectGymData) -> Unit
)

fun UserClimbedGym.toSelectGymData(
    onClickListener: (SelectGymData) -> Unit
) = SelectGymData(
    id = userId,
    name = gymName,
    onClickListener = onClickListener
)
