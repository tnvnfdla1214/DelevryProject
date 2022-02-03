package com.example.delevryproject.ui.home.restaurant.fragment

import androidx.lifecycle.*
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.repository.restaurant.RestaurantRepository
import com.example.delevryproject.model.restaurant.RestaurantModel
import com.example.delevryproject.ui.base.BaseViewModel
import com.example.delevryproject.ui.home.mylocation.MyLocationViewModel
import com.example.delevryproject.ui.home.restaurant.RestaurantCategory
import com.example.delevryproject.ui.home.restaurant.RestautantFilterOrder
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListFagmentViewModel @AssistedInject constructor(
    @Assisted private val restaurantCategory: RestaurantCategory,
    @Assisted private var locationLatLngEntity: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    private var restaurantFilterOrder: RestautantFilterOrder = RestautantFilterOrder.DEFAULT
) : BaseViewModel(){

    @AssistedFactory
    interface RestaurantListFagmentViewModelFactory {
        fun create(restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity): RestaurantListFagmentViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: RestaurantListFagmentViewModelFactory,
            restaurantCategory: RestaurantCategory,
            locationLatLngEntity: LocationLatLngEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(restaurantCategory,locationLatLngEntity) as T
            }
        }
    }

    private var _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData: LiveData<List<RestaurantModel>>
        get() = _restaurantListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLngEntity)
        val sortedList = when (restaurantFilterOrder) {
            RestautantFilterOrder.DEFAULT -> {
                restaurantList
            }
            RestautantFilterOrder.LOW_DELIVERY_TIP -> {
                restaurantList.sortedBy { it.deliveryTipRange.first }
            }
            RestautantFilterOrder.FAST_DELIVERY -> {
                restaurantList.sortedBy { it.deliveryTimeRange.first }
            }
            RestautantFilterOrder.TOP_RATE -> {
                restaurantList.sortedByDescending { it.grade }
            }
        }
        _restaurantListLiveData.value = sortedList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
        }
    }

    /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
    *  바뀐 위치로 변경 후 다시 fetch
    */
    fun setLocationLatLng(locationLatLngEntity: LocationLatLngEntity) {
        this.locationLatLngEntity = locationLatLngEntity
        fetchData()
    }

    /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
    *  바뀐 필터로 변경 후 다시 fetch
    */
    fun setRestaurantFilterOrder(order: RestautantFilterOrder) {
        this.restaurantFilterOrder = order
        fetchData()
    }
}