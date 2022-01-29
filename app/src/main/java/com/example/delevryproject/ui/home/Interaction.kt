package com.example.delevryproject.ui.home

import android.view.View
import com.example.delevryproject.data.local.model.BannerItem


interface Interaction: View.OnClickListener {
    fun onBannerItemClicked(bannerItem: BannerItem)
}