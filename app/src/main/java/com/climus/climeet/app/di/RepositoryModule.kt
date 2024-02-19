package com.climus.climeet.app.di

import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.data.repository.MainRepositoryImpl
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.data.repository.IntroRepositoryImpl
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
}