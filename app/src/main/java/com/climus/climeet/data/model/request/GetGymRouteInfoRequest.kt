package com.climus.climeet.data.model.request

data class GetGymRouteInfoRequest(
    val page: Int,
    val size: Int,
    val floor: Int? = null,
    val sectorId: Long? = null,
    val difficulty: Int? = null,
    val timePoint: String? = null
)
