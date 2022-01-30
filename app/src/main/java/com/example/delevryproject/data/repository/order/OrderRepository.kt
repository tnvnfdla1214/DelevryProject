package com.example.delevryproject.data.repository.order

import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ): OrderRepositoryImpl.Result

    suspend fun getAllOrderMenus(
        userId: String
    ): OrderRepositoryImpl.Result

}
