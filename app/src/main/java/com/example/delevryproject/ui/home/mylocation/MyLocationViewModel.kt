package com.example.delevryproject.ui.home.mylocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.repository.map.MapRepository
import com.example.delevryproject.data.repository.user.UserRepository
import com.example.delevryproject.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel @AssistedInject constructor(
    @Assisted val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    @AssistedFactory
    interface MyLocationViewModelFactory {
        fun create(mapSearchInfoEntity: MapSearchInfoEntity): MyLocationViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: MyLocationViewModelFactory,
            mapSearchInfoEntity: MapSearchInfoEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(mapSearchInfoEntity) as T
            }
        }
    }

    val myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myLocationStateLiveData.value = MyLocationState.Loading
        myLocationStateLiveData.value = MyLocationState.Success(
            mapSearchInfoEntity
        )
    }

    // 지도를 비추고 있는 카메라가 움직임을 알고 바뀌는 위치를 감지
    fun changeLocationInfo(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            myLocationStateLiveData.value = MyLocationState.Success(
                MapSearchInfoEntity(
                    fullAddress = info.fullAddress ?: "주소 정보 없음",
                    name = info.buildingName ?: "빌딩정보 없음",
                    locationLatLng = locationLatLngEntity
                )
            )
        } ?: kotlin.run {

        }
    }

    // 유저 정보에 저장 된 위치정보와 GPS상에 잡히는 위치정보의 비교를 위해 유저 Repository에 저장된다.
    fun confirmSelectLocation() = viewModelScope.launch {
        when(val data = myLocationStateLiveData.value) {
            is MyLocationState.Success -> {
                userRepository.insertUserLocation(data.mapSearchInfoEntity.locationLatLng)
                myLocationStateLiveData.value = MyLocationState.Confirm(
                    data.mapSearchInfoEntity
                )
            }
        }
    }

}
