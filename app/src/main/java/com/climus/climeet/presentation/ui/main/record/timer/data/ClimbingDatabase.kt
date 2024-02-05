package com.climus.climeet.presentation.ui.main.record.timer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ClimbingRecordData::class, StopwatchStatesData::class], version = 1)
@TypeConverters(DataListConverters::class)
abstract class ClimbingDatabase : RoomDatabase() {
    abstract fun ClimbingRecordDao(): ClimbingRecordDao
    abstract fun StopwatchStatesDao(): StopwatchStatesDao

    companion object {
        private var INSTANCE: ClimbingDatabase? = null

        fun getInstance(context: Context): ClimbingDatabase? {
            if (INSTANCE == null) {
                synchronized(ClimbingDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ClimbingDatabase::class.java, "climbing_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}