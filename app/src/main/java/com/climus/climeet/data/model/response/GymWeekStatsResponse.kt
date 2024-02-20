package com.climus.climeet.data.model.response

data class GymWeekStatsResponse(
    val difficulty: List<DifficultyItems>
)

data class DifficultyItems(
    val climeetDifficultyName: String,
    val gymDifficultyName: String,
    val gymDifficultyColor: String,
    val count: Int,
    val difficulty: Int
)
