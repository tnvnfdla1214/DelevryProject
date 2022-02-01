package com.example.delevryproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.R
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.local.model.BannerItem
import com.example.delevryproject.data.local.model.GridItem
import com.example.delevryproject.data.repository.home.HomeRepository
import com.example.delevryproject.data.repository.map.MapRepository
import com.example.delevryproject.data.repository.user.UserRepository
import com.example.delevryproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val homeRepository: HomeRepository,
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _bannerItemList: MutableLiveData<List<BannerItem>> = MutableLiveData()
    private val _gridItemList: MutableLiveData<List<GridItem>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()

    val bannerItemList: LiveData<List<BannerItem>> //라이브 데이터 한번에 묶기
        get() = _bannerItemList
    val gridItemList: LiveData<List<GridItem>>
        get() = _gridItemList
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    init {
        _currentPosition.value = 0 //이거 필요없을듯 빼줘도 될듯
    }

    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        homeStateLiveData.value = HomeState.Loading

        //UserLocation을 가져온다. 가져온게 없으면 현재 위치로
        val userLocation = userRepository.getUserLocation()
        val currentLocation = userLocation ?: locationLatLngEntity

        val addressInfo = mapRepository.getReverseGeoInformation(currentLocation)
        //addressInfo가 null이 아니면
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
                MapSearchInfoEntity(
                    fullAddress = info.fullAddress ?: "주소 정보 없음",
                    name = info.buildingName ?: "빌딩정보 없음",
                    locationLatLng = currentLocation
                ),
                isLocationSame = userLocation == locationLatLngEntity
            )
        }
        //addressInfo null이면
            ?: kotlin.run {
                homeStateLiveData.value = HomeState.Error(
                    messageId = R.string.can_not_load_address_info
                )
            }
    }

    /* 1-1. 위치 정보 : 위치 정보 감지 및 재설정
    * 상태가 성공이면(받아온 위치 정보가 있으면으로 해석 가능) 위치정보 반환
    */
    fun getMapSearchInfo(): MapSearchInfoEntity? {
        when (val data = homeStateLiveData.value) {
            is HomeState.Success -> {
                return data.mapSearchInfoEntity
            }
        }
        return null
    }




    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun getCurrentPosition() = currentPosition.value

    fun getBannerItems() {
        viewModelScope.launch {
            val bannerItemLiveData = homeRepository.getBannerItems()
            withContext(Main) {
                _bannerItemList.value = bannerItemLiveData
            }
        }
    }

    fun getGridItems() {
        viewModelScope.launch {
            val gridItemLiveData = homeRepository.getGridItems()
            withContext(Main) {
                _gridItemList.value = gridItemLiveData
            }
        }
    }

    companion object { //내 위치 정보
        const val MY_LOCATION_KEY = "MyLocation"
    }
}