package com.climus.climeet.data.local

import androidx.room.*

@Dao
interface ClimbingRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(climbingRecord: ClimbingRecordData)

    @Update
    fun update(climbingRecord: ClimbingRecordData)

    @Delete
    fun delete(climbingRecord: ClimbingRecordData)

    @Query("DELETE FROM climbing_record")
    fun deleteAllRecord()

    @Query("SELECT * FROM climbing_record")
    fun getAllRecord(): List<ClimbingRecordData>

    @Query("SELECT * FROM climbing_record WHERE id = :id")
    fun getRecord(id: Int): ClimbingRecordData
}