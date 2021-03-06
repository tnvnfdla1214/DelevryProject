package com.example.delevryproject.ui.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity
import com.example.delevryproject.data.repository.restaurant.food.RestaurantFoodRepository
import com.example.delevryproject.data.repository.user.UserRepository
import com.example.delevryproject.ui.base.BaseViewModel
import com.example.delevryproject.ui.home.mylocation.MyLocationViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel @AssistedInject constructor(
    @Assisted private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    @AssistedFactory
    interface RestaurantDetailViewModelFactory {
        fun create(restaurantEntity: RestaurantEntity): RestaurantDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: RestaurantDetailViewModelFactory,
            restaurantEntity: RestaurantEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(restaurantEntity) as T
            }
        }
    }

    val restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
        restaurantDetailStateLiveData.value = RestaurantDetailState.Loading

        val foods = restaurantFoodRepository.getFoods(restaurantEntity.restaurantInfoId, restaurantEntity.restaurantTitle)
        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()
        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket = foodMenuListInBasket,
            isLiked = isLiked
        )
    }

    /* 1-2-2. ?????? ?????? : ??????, ???, ??????
    * ??? ??????????????? ???????????? ??????
    */
    fun toggleLikedRestaurant() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                // Usser Repository?????? ?????????????????? ????????? ????????? ?????? ??????
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                } ?:
                // Usser Repository?????? ?????????????????? ????????? ????????? ?????? ??????
                kotlin.run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
            else -> Unit
        }
    }

    /* 1-2-1. ????????????
    * ??????????????? ?????????.
    */
    fun notifyClearBasket() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false, {})
                )
            }
            else -> Unit
        }
    }


    /* 1-2-1. ????????????
    * ??????????????? ?????????.
    */
    fun notifyFoodMenuListInBasket(foodMenu: RestaurantFoodEntity) = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                        add(foodMenu)
                    }
                )
            }
            else -> Unit
        }
    }

    /* 1-2-2-1. ?????? ?????? : ???????????? ??????????????? ??????
    * ?????????????????? ?????? ?????? ??? ?????? ??????????????? ????????? ??????
    */
    fun notifyClearNeedAlertInBasket(isClearNeed: Boolean, afterAction: () -> Unit) = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(isClearNeed, afterAction)
                )
            }
            else -> Unit
        }
    }

    /* 1-2-2. ?????? ?????? : ??????, ???, ??????
    * ????????? ?????? ??????
    */
    // ????????? ????????? ?????? ????????? ????????? ??????
    fun getRestaurantInfo(): RestaurantEntity? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }
    }

    /* 1-2-2. ?????? ?????? : ??????, ???, ??????
    * ???????????? ????????????
    */
    // ????????? ????????? ??????????????? ??????????????? ????????????.
    fun getRestaurantPhoneNumber(): String? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }

    /* 1-2-1. ????????????
    * ??????????????? ????????????.
    */
    fun checkMyBasket() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()
                )
            }
            else -> Unit
        }
    }

}
