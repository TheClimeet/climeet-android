package com.climus.climeet.data.model.request


data class ShortsDetailRequest(
    val climbingGymId: Long,
    val routeId: Long,
    val sectorId: Long,
    val description: String,
    val public: String,
    val soundEnabled: Boolean
)
