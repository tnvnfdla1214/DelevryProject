package com.example.delevryproject.data.entitiy.restaurant

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.delevryproject.data.entitiy.Entity
import com.example.delevryproject.ui.restaurant.RestaurantCategory
import com.example.delevryproject.util.convertor.RoomTypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity(
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?
): Entity, Parcelable