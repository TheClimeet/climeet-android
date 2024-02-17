package com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData

import androidx.room.*
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordData

@Dao
interface ClimbingRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(climbingRecord: ClimbingRecordData)

    @Update
    fun update(climbingRecord: ClimbingRecordData)

    @Delete
    fun delete(climbingRecord: ClimbingRecordData)

    @Query("DELETE FROM climbing_record")
    fun deleteAll()

    @Query("SELECT * FROM climbing_record")
    fun getAll(): List<ClimbingRecordData>

    @Query("SELECT * FROM climbing_record WHERE id = :id")
    fun getRoute(id: Int): ClimbingRecordData
}