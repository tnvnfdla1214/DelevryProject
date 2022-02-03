package com.example.delevryproject.ui.home.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.databinding.ActivityEventBinding
import com.example.delevryproject.ui.base.BaseActivity
import com.example.delevryproject.ui.home.HomeViewModel
import com.example.delevryproject.ui.home.mylocation.MyLocationActivity
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.activity_event.*
@AndroidEntryPoint
class EventActivity : BaseActivity<EventViewModel, ActivityEventBinding>() {
    override val viewModel by viewModels<EventViewModel>()
    override fun getViewBinding(): ActivityEventBinding = ActivityEventBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        iv_close.setOnClickListener {
            finish()
        }
        val map = intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY) as MapSearchInfoEntity

        Log.d("test", map.fullAddress)
    }

    override fun observeData() {

    }

}