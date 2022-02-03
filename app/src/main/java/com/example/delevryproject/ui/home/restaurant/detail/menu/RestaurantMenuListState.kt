package com.example.delevryproject.ui.home.restaurant.detail.menu

import com.example.delevryproject.model.restaurant.FoodModel


sealed class RestaurantMenuListState {

    object Uninitialized: RestaurantMenuListState()

    object Loading: RestaurantMenuListState()

    data class Success(
        val restaurantFoodModelList: List<FoodModel>? = null
    ): RestaurantMenuListState()

}
