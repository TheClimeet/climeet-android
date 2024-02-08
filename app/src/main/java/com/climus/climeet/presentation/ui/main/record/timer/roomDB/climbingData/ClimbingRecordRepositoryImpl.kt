package com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData

import javax.inject.Inject

class ClimbingRecordRepositoryImpl @Inject constructor(
    private val climbingRecordDao: ClimbingRecordDao
) : ClimbingRecordRepository {

    override fun insert(climbingRecordData: ClimbingRecordData) {
        climbingRecordDao.insert(climbingRecordData)
    }

    override fun update(climbingRecordData: ClimbingRecordData) {
        climbingRecordDao.update(climbingRecordData)
    }

    override fun delete(climbingRecordData: ClimbingRecordData) {
        climbingRecordDao.delete(climbingRecordData)
    }

    override fun getAll(): List<ClimbingRecordData> {
        return climbingRecordDao.getAll()
    }
}