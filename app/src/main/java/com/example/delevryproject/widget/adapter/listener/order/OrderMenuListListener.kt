package com.example.delevryproject.widget.adapter.listener.order

import com.example.delevryproject.model.restaurant.FoodModel
import com.example.delevryproject.widget.adapter.listener.AdapterListener

interface OrderMenuListListener: AdapterListener {

    fun onRemoveItem(model: FoodModel)

}
