package com.climus.climeet.app.di

import com.climus.climeet.data.remote.MainApi
import com.climus.climeet.data.remote.IntroApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideIntroApi(retrofit: Retrofit): IntroApi = retrofit.create(IntroApi::class.java)


    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)


}