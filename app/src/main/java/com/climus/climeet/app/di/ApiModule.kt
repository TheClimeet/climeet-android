package com.climus.climeet.app.di

import com.climus.climeet.data.remote.GlobalApi
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

//    @Singleton
//    @Provides
//    fun provideIntroService(retrofit: Retrofit): IntroApi = retrofit.create(IntroApi::class.java)

    @Singleton
    @Provides
    fun provideIntroApi(retrofit: Retrofit): IntroApi {
        return retrofit.create(IntroApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGlobalApi(retrofit: Retrofit): GlobalApi {
        return retrofit.create(GlobalApi::class.java)
    }

}