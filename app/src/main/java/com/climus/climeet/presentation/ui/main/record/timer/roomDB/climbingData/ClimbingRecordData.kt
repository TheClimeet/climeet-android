package com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "climbing_record")
data class ClimbingRecordData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gymId: Long = 0,
    val gymName: String = "",
    val date: String = "",
    val time: String = "",
    val avgDifficulty: Int = 0,
)