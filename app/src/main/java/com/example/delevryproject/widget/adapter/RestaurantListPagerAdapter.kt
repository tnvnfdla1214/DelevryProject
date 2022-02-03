package com.example.delevryproject.widget.adapter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.ui.home.restaurant.fragment.RestaurantListFragment

/*
FragmentStateAdapter 와 FragmentStatePagerAdapter 차이를 알자(정적과 동적)
 */
class RestaurantListPagerAdapter(
    fragmentActivity: FragmentActivity,
    val fragmentList: List<RestaurantListFragment>,
    var locationLatLng: LocationLatLngEntity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}
