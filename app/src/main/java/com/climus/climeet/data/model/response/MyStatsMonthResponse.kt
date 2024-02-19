package com.climus.climeet.data.model.response

data class MyStatsMonthResponse(
    val time: String,
    val totalCompletedCount: Int,
    val attemptRouteCount: Int,
    val difficulty: Map<String, Int>
)
