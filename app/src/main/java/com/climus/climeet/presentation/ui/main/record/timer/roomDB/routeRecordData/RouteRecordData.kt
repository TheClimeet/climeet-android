package com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData

import androidx.room.Entity
import androidx.room.PrimaryKey

// API에 넘길겸 평균완등률 계산을 위한 db

@Entity(tableName = "route_record")
data class RouteRecordData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sectorId: Long = 0,
    val routeId: Long = 0,
    val levelName: String = "",
    val levelColor: String = "",
    val difficulty: Int = 0,
    var attemptCount: Int = 0,
    var isCompleted: Boolean = false
)