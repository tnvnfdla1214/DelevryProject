package com.example.delevryproject.data.repository.eathome

import com.example.delevryproject.data.local.model.WhatToEat
import com.example.delevryproject.data.local.model.data.fakeWhatToEatList

class EatWhatRepositoryImpl : EatWhatRepository {

    override suspend fun getWhatToEatItems(): List<WhatToEat> {
        return fakeWhatToEatList
    }
}