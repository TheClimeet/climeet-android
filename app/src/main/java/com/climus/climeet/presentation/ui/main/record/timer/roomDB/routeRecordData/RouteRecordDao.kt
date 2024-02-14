package com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RouteRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routeRecord: RouteRecordData)

    @Update
    fun update(routeRecord: RouteRecordData)

    @Delete
    fun delete(routeRecord: RouteRecordData)

    @Query("DELETE FROM route_record")
    fun deleteAll()

    @Query("DELETE FROM route_record WHERE id = :id")
    fun deleteRecord(id: Int)

    @Query("SELECT * FROM route_record")
    fun getAllRecord(): List<RouteRecordData>

    @Query("SELECT * FROM route_record WHERE id = :id")
    fun getRecord(id: Int): RouteRecordData

    @Query("SELECT * FROM route_record WHERE sectorId = :sectorId AND routeId = :routeId LIMIT 1")
    fun findExistRecord(sectorId: Long, routeId: Long): RouteRecordData?

    @Query("SELECT AVG(difficulty) FROM route_record WHERE isCompleted = 1")
    fun getAverageDifficultyOfCompleted(): Double

    // 레벨별 평균 완등률 계산
    @Query("SELECT * FROM route_record WHERE levelName IN (SELECT DISTINCT levelName FROM route_record)")
    fun getAllLevelRecord(): List<RouteRecordData>

    @Query("SELECT COUNT(*) FROM route_record WHERE levelName = :level AND isCompleted = 1")
    fun getSuccessCount(level: String): Int

    @Query("SELECT COUNT(*) FROM route_record WHERE levelName = :level")
    fun getAttemptCount(level: String): Int
}