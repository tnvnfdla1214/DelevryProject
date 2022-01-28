package com.example.delevryproject.ui.order.sub

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.delevryproject.R
import com.example.delevryproject.databinding.FragmentShoppingLiveBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingLiveFragment : Fragment(R.layout.fragment_shopping_live) {
    private var _binding: FragmentShoppingLiveBinding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShoppingLiveBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}