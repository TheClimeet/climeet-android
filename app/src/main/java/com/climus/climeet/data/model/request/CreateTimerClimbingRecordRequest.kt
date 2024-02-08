package com.climus.climeet.data.model.request

import java.time.LocalTime

data class CreateTimerClimbingRecordRequest (
    val gymId: Long,
    val date: String,
    val time: LocalTime,
    val avgDifficulty: Int,
    val routeRecordRequestDtoList : List<ClimbingRecord>
)

data class ClimbingRecord(
    val routeId: Long,
    val attemptCount: Int,
    val isCompleted: Boolean
)