package com.example.delevryproject.di

import android.content.Context
import com.example.delevryproject.data.local.preference.AppPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import com.example.delevryproject.data.repository.home.HomeRepository
import com.example.delevryproject.data.repository.home.HomeRepositoryImpl
import com.example.delevryproject.data.repository.eathome.EatWhatRepository
import com.example.delevryproject.data.repository.eathome.EatWhatRepositoryImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


// Repository는 뷰, 뷰 모델과 별개로 존재해야 하며, 어디서든 접근이 가능해야 하므로 Singleton 컴포넌트로 작성
/*
참고로 Repository는 왜 싱글턴 객체여야 하는가?
Repository는 네트워크 작업 혹은 데이터베이스 작업을 위해 만들어진 뷰와 뷰 모델과는 별개의 공간입니다.
만약 싱글턴이 아닌 단순 클래스라고 가정하면, 매번 네트워크 작업 혹은 데이터베이스 작업이 일어날 시
새로운 클래스 객체를 생성한다는 것은 매우 비효율적입니다.
만약 클래스 생성이 오래 걸린다고 가정하면, 네트워크 작업 및 데이터베이스 작업을 하기 위해
클래스 객체를 생성하는 것은 네트워크 처리, 데이터베이스 처리 시간에 더해져 매우 오래 걸릴 것입니다.
그래서 싱글턴 객체로 선언을 하여 항상 어디서든 준비되어 있도록 합니다.
*/
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideHomeRepository(): HomeRepository {
        return HomeRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideEatWhatRepository(): EatWhatRepository {
        return EatWhatRepositoryImpl()
    }
    //ApplicationContext 넣는법
    @Singleton
    @Provides
    fun provideAppPreferenceManager(@ApplicationContext context: Context) : AppPreferenceManager
        = AppPreferenceManager(context)

}