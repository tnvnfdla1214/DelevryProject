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

    /* 1-2-2. 가게 상세 : 전화, 찜, 공유
    * 찜 선택했다가 안했다가 토글
    */
    fun toggleLikedRestaurant() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                // Usser Repository에서 해당음식점을 좋아요 한적이 없는 경우
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                } ?:
                // Usser Repository에서 해당음식점을 좋아요 한적이 있는 경우
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

    /* 1-2-1. 장바구니
    * 장바구니를 비운다.
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


    /* 1-2-1. 장바구니
    * 장바구니에 담았다.
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

    /* 1-2-2-1. 메뉴 나열 : 클릭하면 장바구니에 담기
    * 다른가게에서 메뉴 담을 때 확인 다이얼로그 띄울때 사용
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

    /* 1-2-2. 가게 상세 : 전화, 찜, 공유
    * 음식점 정보 공유
    */
    // 음식점 공유를 위해 음식점 정보를 반환
    fun getRestaurantInfo(): RestaurantEntity? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }
    }

    /* 1-2-2. 가게 상세 : 전화, 찜, 공유
    * 전화번호 가져오기
    */
    // 음식점 정보가 잘들어오면 전화번호를 넘겨준다.
    fun getRestaurantPhoneNumber(): String? {
        return when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }

    /* 1-2-1. 장바구니
    * 장바구니를 불러왔다.
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
