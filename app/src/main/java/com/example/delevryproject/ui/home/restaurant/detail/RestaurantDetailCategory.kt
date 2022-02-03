package com.example.delevryproject.ui.home.restaurant.detail

import androidx.annotation.StringRes
import com.example.delevryproject.R


enum class RestaurantDetailCategory(
    @StringRes val categoryNameId: Int
) {

    MENU(R.string.menu), REVIEW(R.string.review)

}
