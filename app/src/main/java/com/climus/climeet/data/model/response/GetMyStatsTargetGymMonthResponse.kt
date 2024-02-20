package com.climus.climeet.data.model.response

data class GetMyStatsTargetGymMonthResponse(
    val time: String,
    val totalCompletedCount: Int,
    val attemptRouteCount: Int,
    val difficulty: List<StatisticsDifficultyItem>
)
