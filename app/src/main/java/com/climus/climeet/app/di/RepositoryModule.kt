package com.climus.climeet.app.di

import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.data.repository.MainRepositoryImpl
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.data.repository.IntroRepositoryImpl
import com.climus.climeet.presentation.ui.main.record.timer.data.ClimbingRecordRepository
import com.climus.climeet.presentation.ui.main.record.timer.data.ClimbingRecordRepositoryImpl
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesRepository
import com.climus.climeet.presentation.ui.main.record.timer.data.StopwatchStatesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindIntroRepository(introRepositoryImpl: IntroRepositoryImpl): IntroRepository

    @Singleton
    @Binds
    abstract fun bindMainRepository(globalRepositoryImpl: MainRepositoryImpl): MainRepository

    @Singleton
    @Binds
    abstract fun bindStopwatchStatesRepository(stopwatchRepositoryImpl: StopwatchStatesRepositoryImpl): StopwatchStatesRepository

    @Singleton
    @Binds
    abstract fun bindClimbingRecordRepository(climbingRecordRepositoryImpl: ClimbingRecordRepositoryImpl): ClimbingRecordRepository
}