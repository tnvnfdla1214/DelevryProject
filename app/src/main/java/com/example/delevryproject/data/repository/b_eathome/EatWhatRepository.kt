package com.example.delevryproject.data.repository.b_eathome

import com.example.delevryproject.data.model.WhatToEat

interface EatWhatRepository {
    suspend fun getWhatToEatItems(): List<WhatToEat>
}