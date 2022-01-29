package com.example.delevryproject.ui.order.sub.bmart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.databinding.FragmentBMartBinding
import com.example.delevryproject.databinding.FragmentFavoriteBinding
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.favorite.FavoriteViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BMartFragment : BaseFragment<BMartViewModel, FragmentBMartBinding>() {
    override val viewModel by viewModels<BMartViewModel>()
    override fun getViewBinding(): FragmentBMartBinding = FragmentBMartBinding.inflate(layoutInflater)

//    private var _binding: FragmentBMartBinding? = null
//    private val binding
//        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //_binding = FragmentBMartBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }

    override fun observeData() {

    }
}