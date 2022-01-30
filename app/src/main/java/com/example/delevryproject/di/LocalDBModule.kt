package com.example.delevryproject.di

import android.content.Context
import com.example.delevryproject.data.local.preference.AppPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {

    //ApplicationContext 넣는법
    @Singleton
    @Provides
    fun provideAppPreferenceManager(@ApplicationContext context: Context) : AppPreferenceManager
            = AppPreferenceManager(context)

}