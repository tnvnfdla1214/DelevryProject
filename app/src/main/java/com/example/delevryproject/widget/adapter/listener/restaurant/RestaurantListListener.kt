package com.example.delevryproject.widget.adapter.listener.restaurant

import com.example.delevryproject.model.restaurant.RestaurantModel
import com.example.delevryproject.widget.adapter.listener.AdapterListener

interface RestaurantListListener: AdapterListener {

    fun onClickItem(model: RestaurantModel)

}