package com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData

interface RouteRecordRepository {
    fun insert(routeRecord: RouteRecordData)

    fun update(routeRecord: RouteRecordData)

    fun delete(routeRecord: RouteRecordData)

    fun deleteById(id: Int)

    fun getAllRecord(): List<RouteRecordData>

    fun getRecord(id: Int): RouteRecordData

    fun getSuccessCount(level: String): Int

    fun getAttemptCount(level: String): Int
}