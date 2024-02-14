package com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData

import javax.inject.Inject

class RouteRecordRepositoryImpl @Inject constructor(
    private val routeRecordDao: RouteRecordDao
) :
    RouteRecordRepository {
    override fun insert(routeRecord: RouteRecordData) {
        routeRecordDao.insert(routeRecord)
    }

    override fun update(routeRecord: RouteRecordData) {
        routeRecordDao.update(routeRecord)
    }

    override fun delete(routeRecord: RouteRecordData) {
        routeRecordDao.delete(routeRecord)
    }

    override fun deleteAll() {
        routeRecordDao.deleteAll()
    }

    override fun deleteById(id: Int) {
        routeRecordDao.deleteRecord(id)
    }

    override fun getAllRecord(): List<RouteRecordData> {
        return routeRecordDao.getAllRecord()
    }

    override fun getRecord(id: Int): RouteRecordData {
        return routeRecordDao.getRecord(id)
    }

    override fun findExistRecord(sectorId: Long, routeId: Long): RouteRecordData? {
        return routeRecordDao.findExistRecord(sectorId, routeId)
    }

    override fun getAverageDifficultyOfCompleted(): Double {
        return routeRecordDao.getAverageDifficultyOfCompleted()
    }

    override fun getSuccessCount(level: String): Int{
        return routeRecordDao.getSuccessCount(level)
    }

    override fun getAttemptCount(level: String): Int{
        return routeRecordDao.getAttemptCount(level)
    }
}