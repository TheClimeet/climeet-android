package com.climus.climeet.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Singleton
//    @Binds
//    abstract fun bindIntroRepository(
//        introRepositoryImpl: IntroRepositoryImpl
//    ): IntroRepository
}