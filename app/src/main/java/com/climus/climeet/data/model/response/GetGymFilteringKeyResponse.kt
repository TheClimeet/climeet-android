package com.climus.climeet.data.model.response

data class GetGymFilteringKeyResponse(
    val difficultyList: List<DifficultyItem>,
    val maxFloor: Int,
    val gymId: Int,
    val layoutImageUrl: String? = "",
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
    val sectorId: Long,
    val floor: Int,
    val name: String,
    val sectorImageUrl: String
)