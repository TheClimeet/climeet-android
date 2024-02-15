package com.climus.climeet.presentation.ui.main.record.timer.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordDao
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordData
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordDao
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordData

@Database(entities = [ClimbingRecordData::class, RouteRecordData::class], version = 2, exportSchema = false)
abstract class ClimbingDatabase : RoomDatabase() {
    abstract fun ClimbingRecordDao(): ClimbingRecordDao
    abstract fun RouteRecordDao(): RouteRecordDao

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