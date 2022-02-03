package com.example.delevryproject.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity
import com.example.delevryproject.data.local.db.dao.FoodMenuBasketDao
import com.example.delevryproject.data.local.db.dao.LocationDao
import com.example.delevryproject.data.local.db.dao.RestaurantDao


@Database(
    entities = [LocationLatLngEntity::class, RestaurantFoodEntity::class, RestaurantEntity::class],
    version = 1,
    exportSchema = false
)
/* 4. 기본 설정 및 data :
*  현재 위치, 장바구니의 담겨 있는 메뉴들, 그 메뉴들의 가게 정보가 담겨 있다.
*/
abstract class ApplicationDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

    abstract fun LocationDao(): LocationDao

    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao

    abstract fun RestaurantDao(): RestaurantDao

}
