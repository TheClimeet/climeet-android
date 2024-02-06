package com.climus.climeet.data.model.request

data class ShortsUploadRequest(
    val video: String,
    val thumbnailImage: String,
    val createShortsRequest: ShortsDetailRequest
)

data class ShortsDetailRequest(
    val climbingGymId: Long,
    val routeId: Long,
    val sectorId: Long,
    val description: String,
    val public: Boolean,
    val soundEnabled: Boolean
)
