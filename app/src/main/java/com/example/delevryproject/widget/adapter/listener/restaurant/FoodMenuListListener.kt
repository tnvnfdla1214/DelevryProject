package com.example.delevryproject.widget.adapter.listener.restaurant

import com.example.delevryproject.model.restaurant.FoodModel
import com.example.delevryproject.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {

    fun onClickItem(model: FoodModel)

}
