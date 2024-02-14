package com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData

interface ClimbingRecordRepository {
    fun insert(climbingRecordData: ClimbingRecordData)
    fun update(climbingRecordData: ClimbingRecordData)
    fun delete(climbingRecordData: ClimbingRecordData)
    fun deleteAll()
    fun getAll(): List<ClimbingRecordData>
    fun getRoute(id: Int): ClimbingRecordData
}