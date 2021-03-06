package com.example.delevryproject.data.repository.home

import com.example.delevryproject.data.local.model.BannerItem
import com.example.delevryproject.data.local.model.GridItem
import com.example.delevryproject.data.local.model.data.fakeBannerItemList
import com.example.delevryproject.data.local.model.data.fakeGridItemList

//singleton
//suspend가 붙는 이유는 저희는 coroutine을 사용하기 때문
class HomeRepositoryImpl : HomeRepository {
    override suspend fun getBannerItems(): List<BannerItem> {
        return fakeBannerItemList
    }

    override suspend fun getGridItems(): List<GridItem> {
        return fakeGridItemList
    }

}