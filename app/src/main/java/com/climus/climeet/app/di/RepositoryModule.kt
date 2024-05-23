package com.climus.climeet.app.di

import com.climus.climeet.data.repository.AuthRepository
import com.climus.climeet.data.repository.IntroRepository
import com.climus.climeet.data.repository.IntroRepositoryImpl
import com.climus.climeet.data.repository.MainRepository
import com.climus.climeet.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindIntroRepository(introRepositoryImpl: IntroRepositoryImpl): IntroRepository

    @Binds
    abstract fun bindMainRepository(globalRepositoryImpl: MainRepositoryImpl): MainRepository

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: IntroRepositoryImpl): AuthRepository
}