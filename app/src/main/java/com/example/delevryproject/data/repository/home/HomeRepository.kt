package com.example.delevryproject.data.repository.home

import com.example.delevryproject.data.local.model.BannerItem
import com.example.delevryproject.data.local.model.GridItem

//interface와 Impl로 나눈 이유는 1.가독성 2. Testing용이(mock)
interface HomeRepository {
    suspend fun getBannerItems(): List<BannerItem>
    suspend fun getGridItems(): List<GridItem>
}