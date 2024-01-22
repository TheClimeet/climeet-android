package com.climus.climeet.app.di

import com.climus.climeet.data.remote.GlobalApi
import com.climus.climeet.data.repository.GlobalRepository
import com.climus.climeet.data.repository.GlobalRepositoryImpl
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.data.repository.IntroRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
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
    abstract fun bindGlobalRepository(globalRepositoryImpl: GlobalRepositoryImpl): GlobalRepository


}