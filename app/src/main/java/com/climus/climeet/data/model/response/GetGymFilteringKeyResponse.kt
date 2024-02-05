package com.climus.climeet.data.model.response

data class GetGymFilteringKeyResponse(
    val difficultyList: List<DifficultyItem>,
    val floorList: List<Int>,
    val gymId: Int,
    val layoutImageUrl: String?="",
    val sectorList: List<SectorItem>,
    val timePoint: String
)

data class DifficultyItem(
    val climeetDifficultyName: String,
    val difficulty: Int,
    val gymDifficultyColor: String,
    val gymDifficultyName: String
)

data class SectorItem(
    val floor: Int,
    val name: String,
    val sectorImageUrl: String
)