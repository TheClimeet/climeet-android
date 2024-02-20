package com.climus.climeet.data.model.response

data class GetClimberProfileStatisticsResponse(
    val userId: Long,
    val totalCompletedCount: Int? = 1,
    val attemptRouteCount: Int? = 0,
    val difficulty: Map<String, Int>
)
