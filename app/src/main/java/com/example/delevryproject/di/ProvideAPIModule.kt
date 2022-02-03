package com.example.delevryproject.di

import android.content.Context
import com.example.delevryproject.BuildConfig
import com.example.delevryproject.data.remote.network.FoodApiService
import com.example.delevryproject.data.remote.network.MapApiService
import com.example.delevryproject.data.remote.url.Url
import com.example.delevryproject.ui.home.restaurant.RestautantFilterOrder
import com.example.delevryproject.util.provider.ResourcesProvider
import com.example.delevryproject.util.provider.ResourcesProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvideAPIModule {

    @Singleton
    @Provides
    fun provideRestautantFilterOrder(): RestautantFilterOrder {
        return RestautantFilterOrder.DEFAULT
    }

    @Singleton
    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourcesProvider {
        return ResourcesProviderImpl(context)
    }

    /* 기본 설정 및 data :
    *  통신에 관련된 기능들
    */

    @Singleton
    @Provides
    fun provideMapApiService(@MapAPIService retrofit: Retrofit): MapApiService {
        return retrofit.create(MapApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideFoodApiService(@FoodAPIService retrofit: Retrofit): FoodApiService {
        return retrofit.create(FoodApiService::class.java)
    }

    /* 4. 기본 설정 및 data :
    *  지도와 음식을 가져오는 통신
    */
    @Singleton
    @Provides
    @MapAPIService
    fun provideMapRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.TMAP_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @FoodAPIService
    fun provideFoodRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.FOOD_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    // Gson에 대한 의존성
    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    //retrofit을 쓰기위한 okhttp변환에 필요한 요소
    @Singleton
    @Provides
    fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MapAPIService

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class FoodAPIService
}