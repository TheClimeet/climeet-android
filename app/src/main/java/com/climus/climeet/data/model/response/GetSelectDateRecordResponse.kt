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
        val hour = if (localTime.hour == 0) 0 else localTime.hour
        val minute = if (localTime.minute == 0) 0 else localTime.minute
        val second = if (localTime.second == 0) 0 else localTime.second
        return "${hour}h ${minute}m ${second}s"
    }
}
