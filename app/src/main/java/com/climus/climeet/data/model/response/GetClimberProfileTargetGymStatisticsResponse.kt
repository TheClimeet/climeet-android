package com.climus.climeet.data.model.response

data class GetClimberProfileTargetGymStatisticsResponse(
    val userId: Long,
    val totalCompletedCount: Int,
    val attemptRouteCount: Int,
    val difficulty: List<StatisticsDifficultyItem>
)

data class StatisticsDifficultyItem(
    val climeetDifficultyName: String,
    val gymDifficultyName: String,
    val gymDifficultyColor: String,
    val count: Int,
    val difficulty: Int
)
