package com.climus.climeet.data.model.request

data class GetGymRouteInfoRequest(
    val page: Int,
    val size: Int,
    val floorList: List<Int>? = null,
    val sectorIdList: List<Long>? = null,
    val difficultyList: List<Int>? = null,
    val timePoint: String? = null
)
