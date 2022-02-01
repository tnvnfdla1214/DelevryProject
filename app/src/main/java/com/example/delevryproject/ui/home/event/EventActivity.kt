package com.example.delevryproject.ui.home.event

import android.os.Bundle
import androidx.activity.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.databinding.ActivityEventBinding
import com.example.delevryproject.ui.base.BaseActivity
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
    }

    override fun observeData() {

    }
}