package com.example.delevryproject.widget.adapter.listener.restaurant

import com.example.delevryproject.model.restaurant.RestaurantModel

interface RestaurantLikeListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)

}
