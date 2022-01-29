package com.example.delevryproject.data.repository.eathome

import com.example.delevryproject.data.local.model.WhatToEat

interface EatWhatRepository {
    suspend fun getWhatToEatItems(): List<WhatToEat>
}