package com.climus.climeet.presentation.ui.main.global

import com.climus.climeet.R
import com.climus.climeet.data.model.response.DifficultyItem
import com.climus.climeet.data.model.response.RouteItem
import com.climus.climeet.data.model.response.SectorItem
import com.climus.climeet.presentation.ui.main.global.selectsector.model.RouteUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.GymLevelUiData
import com.climus.climeet.presentation.ui.main.global.selectsector.model.SectorNameUiData
import com.climus.climeet.presentation.util.Constants

fun SectorItem.toSectorNameUiData(
    onClickListener: (SectorNameUiData) -> Unit
) = SectorNameUiData(
    sectorId = sectorId,
    name = name,
    floor = floor,
    onClickListener = onClickListener
)

fun DifficultyItem.toGymLevelUiData(
    onClickListener: (GymLevelUiData) -> Unit
) = GymLevelUiData(
    levelName = gymDifficultyName,
    levelColor = gymDifficultyColor,
    difficulty = difficulty,
    onClickListener = onClickListener
)

fun RouteItem.toRouteUiData(
    onClickListener: (RouteUiData) -> Unit
) : RouteUiData{

    val holdColor = Constants.holdColor[holdColor] ?: run {
        R.drawable.ic_white_hold
    }
    
    return RouteUiData(
        routeId = routeId,
        sectorId = sectorId,
        sectorName = sectorName,
        difficulty = difficulty,
        gymLevelName = gymDifficultyName,
        gymLevelColor = gymDifficultyColor,
        climeetLevelName = climeetDifficultyName,
        routeImg = routeImageUrl,
        holdImg = holdColor,
        onClickListener = onClickListener
    )
}