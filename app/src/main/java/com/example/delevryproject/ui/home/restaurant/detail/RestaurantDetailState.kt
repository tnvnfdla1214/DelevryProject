package com.example.delevryproject.ui.home.restaurant.detail

import com.example.delevryproject.data.entitiy.restaurant.RestaurantEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity


sealed class RestaurantDetailState {

    object Uninitialized: RestaurantDetailState()

    object Loading: RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList: List<RestaurantFoodEntity>? = null,
        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null,
        val isClearNeedInBasketAndAction: Pair<Boolean, () -> Unit> = Pair(false, {}),
        val isLiked: Boolean? = null
    ): RestaurantDetailState()

}
