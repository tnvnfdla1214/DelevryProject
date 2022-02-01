package com.example.delevryproject.ui.home.mylocation

import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity


sealed class MyLocationState {

    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

}
