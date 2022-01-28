package com.example.delevryproject.data.repository.b_eathome

import com.example.delevryproject.data.model.WhatToEat
import com.example.delevryproject.data.model.data.fakeWhatToEatList

class EatWhatRepositoryImpl : EatWhatRepository {

    override suspend fun getWhatToEatItems(): List<WhatToEat> {
        return fakeWhatToEatList
    }
}