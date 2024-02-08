package com.climus.climeet.data.model.response

import java.time.LocalTime

data class GetSelectDateRecordResponse(
    val climbingRecordId: Long,
    val date: String,
    val time: String,
    val totalCompletedCount: Int,
    val totalAttemptCount: Int,
    val avgDifficulty: Int,
    val gymId: Long
) {
    fun getFormattedTime(): String {
        val localTime = LocalTime.parse(time)
        val hour = localTime.hour
        val minute = localTime.minute
        return "${hour.toString().trimStart('0')}h ${minute.toString().trimStart('0')}m"
    }
}
