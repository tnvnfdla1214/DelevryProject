package com.example.delevryproject.widget.adapter.listener.order

import com.example.delevryproject.widget.adapter.listener.AdapterListener


interface OrderListListener: AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)

}
