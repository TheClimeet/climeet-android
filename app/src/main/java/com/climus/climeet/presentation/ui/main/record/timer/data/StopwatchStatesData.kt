package com.climus.climeet.presentation.ui.main.record.timer.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stopwatch_state")
data class StopwatchStatesData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isStart: Boolean = false,
    val isPaused: Boolean = false,
    val isRestart: Boolean = false,
    val isStop: Boolean = true,
    val isRunning: Boolean = false
)
