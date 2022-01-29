package com.example.delevryproject.ui.event

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.delevryproject.R
import com.example.delevryproject.databinding.ActivityEventBinding
import com.example.delevryproject.databinding.ActivityMainBinding
import com.example.delevryproject.ui.base.BaseActivity
import com.example.delevryproject.ui.main.MainViewModel
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