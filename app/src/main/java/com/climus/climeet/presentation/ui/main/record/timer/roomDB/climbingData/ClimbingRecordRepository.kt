package com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData

interface ClimbingRecordRepository {
    fun insert(climbingRecordData: ClimbingRecordData)
    fun update(climbingRecordData: ClimbingRecordData)
    fun delete(climbingRecordData: ClimbingRecordData)
    fun getAll(): List<ClimbingRecordData>
}