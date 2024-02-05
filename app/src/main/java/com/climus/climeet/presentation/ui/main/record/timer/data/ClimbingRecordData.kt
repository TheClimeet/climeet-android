package com.climus.climeet.presentation.ui.main.record.timer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "climbing_record")
data class ClimbingRecordData(
    @PrimaryKey(autoGenerate = true)
    val gymId: Int = 0,
    val date: String = "",
    val time: String = "",
    val avgDifficulty: Int = 0,
    val routeRecordRequestDtoList : List<ClimbingRecord>
)

data class ClimbingRecord(
    val routeId: Int = 0,
    val attemptCount: Int = 0,
    val isCompleted: Boolean = false
)