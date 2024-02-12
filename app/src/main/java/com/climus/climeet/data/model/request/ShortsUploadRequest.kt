package com.climus.climeet.data.model.request


data class ShortsDetailRequest(
    val public: String,
    val thumbnailImageUrl: String,
    val description: String,
    val soundEnabled: Boolean,
    val climbingGymId: Long?=null,
    val routeId: Long?=null,
    val sectorId: Long?=null,
)
