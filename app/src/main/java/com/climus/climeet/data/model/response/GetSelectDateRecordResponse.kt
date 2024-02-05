package com.climus.climeet.data.model.response

import java.time.LocalTime

data class GetSelectDateRecordResponse(
    val climbingRecordId: Long,
    val date: String,
    val time: LocalTime,
    val totalCompletedCount: Int,
    val totalAttemptCount: Int,
    val avgDifficulty: Int,
    val gymId: Long
)
