package com.example.delevryproject.di

import android.content.Context
import androidx.room.Room
import com.example.delevryproject.data.local.db.ApplicationDatabase
import com.example.delevryproject.data.local.db.dao.FoodMenuBasketDao
import com.example.delevryproject.data.local.db.dao.LocationDao
import com.example.delevryproject.data.local.db.dao.RestaurantDao
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

    @Provides
    @Singleton
    fun providesLocationDao(applicationDatabase: ApplicationDatabase): LocationDao = applicationDatabase.LocationDao()

    @Provides
    @Singleton
    fun providesRestaurantDao(applicationDatabase: ApplicationDatabase): RestaurantDao = applicationDatabase.RestaurantDao()

    @Provides
    @Singleton
    fun providesFoodMenuBasketDao(applicationDatabase: ApplicationDatabase): FoodMenuBasketDao = applicationDatabase.FoodMenuBasketDao()

    @Provides
    @Singleton
    fun providesApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase
            = Room.databaseBuilder(context, ApplicationDatabase::class.java,"ToDoDatabase").build()

}