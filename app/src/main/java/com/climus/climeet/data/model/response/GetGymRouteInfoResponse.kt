package com.climus.climeet.data.model.response

data class GetGymRouteInfoResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<RouteItem>
)

data class RouteItem(
    val routeId: Long,
    val sectorId: Long,
    val sectorName: String,
    val climeetDifficultyName: String,
    val difficulty: Int,
    val gymDifficultyName: String,
    val gymDifficultyColor: String,
    val routeImageUrl: String
)
