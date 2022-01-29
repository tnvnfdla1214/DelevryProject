package com.example.delevryproject.data.entitiy.order

import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity


data class OrderEntity(
    val id: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>,
    val restaurantTitle: String
)
