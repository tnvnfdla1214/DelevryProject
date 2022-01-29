package com.example.delevryproject.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.delevryproject.databinding.FragmentOrderBinding
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.order.sub.bmart.BMartFragment
import com.example.delevryproject.ui.order.sub.orderlist.OrderListFragment
import com.example.delevryproject.ui.order.sub.shoppinglive.ShoppingLiveFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class OrderFragment : BaseFragment<OrderViewModel, FragmentOrderBinding>() {

    override val viewModel by viewModels<OrderViewModel>()

    override fun getViewBinding(): FragmentOrderBinding = FragmentOrderBinding.inflate(layoutInflater)



    private var _binding: FragmentOrderBinding? = null
//    private val binding
//        get() = _binding!!

    private val title = arrayOf("배달﹒포장", "B마트", "쇼핑라이브")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //_binding = FragmentOrderBinding.bind(view)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        binding.pager.apply {
            adapter = object : FragmentStateAdapter(requireActivity()) {
                override fun getItemCount(): Int {
                    return title.size
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> OrderListFragment()
                        1 -> BMartFragment()
                        2 -> ShoppingLiveFragment()
                        else -> OrderListFragment()
                    }
                }
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.pager){tab, position ->
            tab.text = title[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun observeData() {

    }
}