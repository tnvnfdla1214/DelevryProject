package com.example.delevryproject.ui.eatwhat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delevryproject.databinding.FragmentEatWhatBinding
import com.example.delevryproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.fragment_eat_what.*
@AndroidEntryPoint
class EatWhatFragment : BaseFragment<EatWhatViewModel, FragmentEatWhatBinding>() {
    override val viewModel by viewModels<EatWhatViewModel>()
    override fun getViewBinding(): FragmentEatWhatBinding = FragmentEatWhatBinding.inflate(layoutInflater)

    private lateinit var whatToEatAdapter: WhatToEatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFakeWhatToEatList()

        whatToEatAdapter = WhatToEatAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = whatToEatAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.eatWhatToEatList.observe(
            viewLifecycleOwner,
            Observer { fakeWhatToEatList ->
                fakeWhatToEatList?.let {
                    whatToEatAdapter.apply {
                        setList(fakeWhatToEatList)
                    }
                }
            })
    }

    override fun observeData() {

    }
}