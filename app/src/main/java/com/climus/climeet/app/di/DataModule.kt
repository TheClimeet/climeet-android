package com.climus.climeet.app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.climus.climeet.app.App.Companion.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences("APP", Application.MODE_PRIVATE)

}