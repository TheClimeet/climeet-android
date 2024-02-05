package com.climus.climeet.presentation.ui.main.record.timer.data

import androidx.room.*


@Dao
interface StopwatchStatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatch: StopwatchStatesData)

    @Update
    fun update(stopwatch: StopwatchStatesData)

    @Delete
    fun delete(stopwatch: StopwatchStatesData)

    @Query("SELECT * FROM stopwatch_state")
    fun getAll(): List<StopwatchStatesData>

    @Query("SELECT * FROM stopwatch_state WHERE id = :id")
    fun getState(id: Int): StopwatchStatesData

    @Query("INSERT INTO stopwatch_state DEFAULT VALUES")
    fun initialize()
}