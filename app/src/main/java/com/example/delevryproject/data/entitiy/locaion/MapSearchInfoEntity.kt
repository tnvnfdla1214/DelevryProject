package com.example.delevryproject.data.entitiy.locaion

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//LocationLatLngEntity를 mapAPIService로 주소를 구해서 이제 갖다쓰려고 담아놓는 객체
@Parcelize
data class MapSearchInfoEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
): Parcelable
