package com.climus.climeet.app.di

import android.content.Context
import androidx.room.Room
import com.climus.climeet.presentation.ui.main.record.timer.data.ClimbingRecordDao
import com.climus.climeet.presentation.ui.main.record.timer.data.ClimbingDatabase
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "climeet.db"

    @Provides
    @Singleton
    fun provideRecordDatabase(@ApplicationContext context: Context): ClimbingDatabase {
        return Room.databaseBuilder(context, ClimbingDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideClimbingRecordDao(recordDatabase: ClimbingDatabase): ClimbingRecordDao {
        return recordDatabase.ClimbingRecordDao()
    }

    @Provides
    @Singleton
    fun provideStopwatchStatesDao(recordDatabase: ClimbingDatabase): StopwatchStatesDao {
        return recordDatabase.StopwatchStatesDao()
    }
}