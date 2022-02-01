package com.example.delevryproject.ui.home

import androidx.annotation.StringRes
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity


sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity,
        val isLocationSame: Boolean,
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()

}
