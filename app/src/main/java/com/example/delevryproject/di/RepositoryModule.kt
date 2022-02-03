package com.example.delevryproject.di

import com.example.delevryproject.data.local.db.dao.FoodMenuBasketDao
import com.example.delevryproject.data.local.db.dao.LocationDao
import com.example.delevryproject.data.local.db.dao.RestaurantDao
import com.example.delevryproject.data.remote.network.FoodApiService
import com.example.delevryproject.data.remote.network.MapApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import com.example.delevryproject.data.repository.home.HomeRepository
import com.example.delevryproject.data.repository.home.HomeRepositoryImpl
import com.example.delevryproject.data.repository.eathome.EatWhatRepository
import com.example.delevryproject.data.repository.eathome.EatWhatRepositoryImpl
import com.example.delevryproject.data.repository.map.MapRepository
import com.example.delevryproject.data.repository.map.MapRepositoryImpl
import com.example.delevryproject.data.repository.order.OrderRepository
import com.example.delevryproject.data.repository.order.OrderRepositoryImpl
import com.example.delevryproject.data.repository.restaurant.RestaurantRepository
import com.example.delevryproject.data.repository.restaurant.RestaurantRepositoryImpl
import com.example.delevryproject.data.repository.restaurant.food.RestaurantFoodRepository
import com.example.delevryproject.data.repository.restaurant.food.RestaurantFoodRepositoryImpl
import com.example.delevryproject.data.repository.user.UserRepository
import com.example.delevryproject.data.repository.user.UserRepositoryImpl
import com.example.delevryproject.util.provider.ResourcesProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher


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

    @Singleton
    @Provides
    fun provideOrderRepository(@DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
                               firestore: FirebaseFirestore
    ): OrderRepository = OrderRepositoryImpl(ioDispatcher,firestore)

    @Singleton
    @Provides
    fun provideUserRepository(locationDao: LocationDao,
                              restaurantDao: RestaurantDao,
                              @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher): UserRepository
    = UserRepositoryImpl(locationDao,restaurantDao,ioDispatcher)

    @Singleton
    @Provides
    fun provideMapRepository(mapApiService: MapApiService,
                             @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher): MapRepository
            = MapRepositoryImpl(mapApiService,ioDispatcher)

    @Singleton
    @Provides
    fun provideRestaurantFoodRepository (
        foodApiService: FoodApiService,
        foodMenuBasketDao: FoodMenuBasketDao,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RestaurantFoodRepository
            = RestaurantFoodRepositoryImpl(foodApiService,foodMenuBasketDao,ioDispatcher)

    @Singleton
    @Provides
    fun provideRestaurantRepository(
        mapApiService: MapApiService,
        resourcesProvider: ResourcesProvider,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RestaurantRepository
            = RestaurantRepositoryImpl(mapApiService,resourcesProvider,ioDispatcher)

}