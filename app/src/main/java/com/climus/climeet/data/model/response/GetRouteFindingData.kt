package com.climus.climeet.data.model.response

import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLayoutItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiLevelItem
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiRouteChipData
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.UiSectorItem
import com.climus.climeet.presentation.util.Constants

data class GetRouteFindingData(
    val gymId: String,
    val timePoint: String,
    val maxFloor: Int,
    val difficultyList: List<difficultyItem>? = emptyList(),
    val layoutList: List<layoutItem>? = emptyList(),
    val sectorList: List<sectorItem>? = emptyList(),
    val routeList: List<routeItem>? = emptyList()
) {
    data class difficultyItem(
        val climeetDifficultyName: String,
        val gymDifficultyName: String,
        val difficulty: Int,
        val gymDifficultyColor: String
    ) {
        fun toUiLevelItem(setLevelListener: (String, String) -> Unit) = UiLevelItem(
            colorName = gymDifficultyName,
            colorHex = gymDifficultyColor,
            climeetLevel = climeetDifficultyName,
            setLevelListener
        )
    }

    data class layoutItem(
        val id: Int,
        val imgUrl: String,
        val floor: Int
    ) {
        fun toUiLayoutItem() = UiLayoutItem(
            floor = floor,
            gymImg = imgUrl
        )
    }

    data class sectorItem(
        val sectorId: Int,
        val name: String,
        val floor: Int,
        val imgUrl: String
    ) {
        fun toUiSectorItem(setSectorListener: (String, String) -> Unit) = UiSectorItem(
            sectorName = name,
            sectorFloor = floor,
            sectorImg = imgUrl,
            isSelected = false,
            setSectorListener = setSectorListener
        )
    }

    data class routeItem(
        val routeId: Int,
        val sectorId: Int,
        val sectorName: String,
        val climeetDifficultyName: String,
        val difficulty: Int,
        val gymDifficultyName: String,
        val gymDifficultyColor: String,
        val routeImageUrl: String,
        val holdColor: String
    ) {
        fun toUiRouteChipData() = UiRouteChipData(
            sectorName = sectorName,
            gymLevelName = gymDifficultyName,
            gymLevelColor = gymDifficultyColor,
            routeImg = routeImageUrl,
            holdImg = Constants.holdColor[holdColor]?.let { it }.run { 2131231155 },
        )
    }
}


