package com.example.delevryproject.data.repository.restaurant

import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantEntity
import com.example.delevryproject.ui.home.restaurant.RestaurantCategory


interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory: RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ): List<RestaurantEntity>

}
