package com.example.delevryproject.ui.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity
import com.example.delevryproject.data.repository.restaurant.food.RestaurantFoodRepository
import com.example.delevryproject.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RestaurantListViewModel @Inject constructor(
    //private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

//    val foodMenuBasketLiveData = MutableLiveData<List<RestaurantFoodEntity>>()
//
//    // 홈에서 장바구니에 대한 데이터를 갖게하는 메서드
//    fun checkMyBasket() = viewModelScope.launch {
//        foodMenuBasketLiveData.value = restaurantFoodRepository.getAllFoodMenuListInBasket()
//    }
}