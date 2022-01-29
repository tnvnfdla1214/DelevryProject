package com.example.delevryproject.ui.order.sub.shoppinglive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.databinding.FragmentFavoriteBinding
import com.example.delevryproject.databinding.FragmentShoppingLiveBinding
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.favorite.FavoriteViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingLiveFragment : BaseFragment<ShoppingLiveViewModel, FragmentShoppingLiveBinding>() {
    override val viewModel by viewModels<ShoppingLiveViewModel>()

    override fun getViewBinding(): FragmentShoppingLiveBinding = FragmentShoppingLiveBinding.inflate(layoutInflater)

    override fun observeData() {

    }

//    private var _binding: FragmentShoppingLiveBinding? = null
//    private val binding
//        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //_binding = FragmentShoppingLiveBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding=null
    }

}