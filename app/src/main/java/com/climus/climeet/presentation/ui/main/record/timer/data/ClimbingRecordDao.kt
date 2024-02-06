package com.climus.climeet.presentation.ui.main.record.timer.data

import androidx.room.*

@Dao
interface ClimbingRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(climbingRecord: ClimbingRecordData)

    @Update
    fun update(climbingRecord: ClimbingRecordData)

    @Delete
    fun delete(climbingRecord: ClimbingRecordData)

    @Query("SELECT * FROM climbing_record")
    fun getAll(): List<ClimbingRecordData>

    @Query("INSERT INTO climbing_record DEFAULT VALUES")
    fun initialize()
}