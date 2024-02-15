package com.climus.climeet.app.di

import android.content.Context
import androidx.room.Room
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordDao
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.ClimbingDatabase
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordRepository
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.climbingData.ClimbingRecordRepositoryImpl
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordDao
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordRepository
import com.climus.climeet.presentation.ui.main.record.timer.roomDB.routeRecordData.RouteRecordRepositoryImpl
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
        return Room.databaseBuilder(context, ClimbingDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideClimbingRecordDao(recordDatabase: ClimbingDatabase): ClimbingRecordDao {
        return recordDatabase.ClimbingRecordDao()
    }

    @Provides
    @Singleton
    fun provideClimbingRecordRepository(climbingRecordDao: ClimbingRecordDao): ClimbingRecordRepository {
        return ClimbingRecordRepositoryImpl(climbingRecordDao)
    }

    @Provides
    @Singleton
    fun provideRouteRecordDao(recordDatabase: ClimbingDatabase): RouteRecordDao {
        return recordDatabase.RouteRecordDao()
    }

    @Provides
    @Singleton
    fun provideRouteRecordRepository(routeRecordDao: RouteRecordDao): RouteRecordRepository {
        return RouteRecordRepositoryImpl(routeRecordDao)
    }
}