package com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData

interface RouteRecordRepository {
    fun insert(routeRecord: RouteRecordData)

    fun update(routeRecord: RouteRecordData)

    fun delete(routeRecord: RouteRecordData)

    fun deleteAll()

    fun deleteById(id: Int)

    fun getAllRecord(): List<RouteRecordData>

    fun getRecord(id: Int): RouteRecordData

    fun findExistRecord(sectorId: Long, routeId: Long): RouteRecordData?

    fun getAverageDifficultyOfCompleted(): Double

    fun getAllLevelRecord(): List<RouteRecordData>

    fun getSuccessCount(level: String): Int

    fun getAttemptCount(level: String): Int
}