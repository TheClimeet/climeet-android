package com.climus.climeet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// 저장 및 API에 넘길 데이터
// id = 1에 계속 업데이트하며 사용할 예정
@Entity(tableName = "climbing_record")
data class ClimbingRecordData(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val gymId: Long = 0,
    val gymName: String = "",
    val date: String = "",
    val time: String = "",
    val avgDifficulty: Int = 0,
)