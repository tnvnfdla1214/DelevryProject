package com.example.delevryproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton
//나중에 main모듈도 넣기
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @IoDispatcher
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
    @Provides
    @MainDispatcher
    @Singleton
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MainDispatcher
}
