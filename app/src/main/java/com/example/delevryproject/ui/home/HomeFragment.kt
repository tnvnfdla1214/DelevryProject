package com.example.delevryproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.delevryproject.R
import com.example.delevryproject.data.local.model.BannerItem
import com.example.delevryproject.databinding.FragmentHomeBinding
import com.example.delevryproject.ui.event.EventActivity
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.extension.collapse
import com.example.delevryproject.extension.expand

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), Interaction {

    override val viewModel by viewModels<HomeViewModel>()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_see_detail.setOnClickListener(this)
        iv_arrow.setOnClickListener(this)

        initViewPager2()
        initGridRecyclerView()
        autoScrollViewPager()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(viewLifecycleOwner, Observer {
            viewPagerAdapter.submitList(it)
        })
        viewModel.gridItemList.observe(viewLifecycleOwner, Observer {
            gridRecyclerViewAdapter.submitList(it)
        })
        viewModel.currentPosition.observe(viewLifecycleOwner, Observer {
            viewPager2.currentItem = it
        })
    }

    private fun autoScrollViewPager() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            while (viewLifecycleOwner.lifecycleScope.isActive) {
                delay(3000)
                viewModel.getCurrentPosition()?.let {
                    viewModel.setCurrentPosition(it.plus(1) % 5)
                }
            }
        }
    }

    private fun initGridRecyclerView() {
        gridRecyclerView.apply {
            gridRecyclerViewAdapter = GridRecyclerViewAdapter()
            layoutManager = GridLayoutManager(this@HomeFragment.context, 4)
            adapter = gridRecyclerViewAdapter

        }
    }

    private fun initViewPager2() {
        viewPager2.apply {
            viewPagerAdapter = ViewPagerAdapter(this@HomeFragment)
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tv_page_number.text = "${position + 1}"
                    viewModel.setCurrentPosition(position)
                }
            })
        }
    }

    override fun onBannerItemClicked(bannerItem: BannerItem) {
        startActivity(Intent(this@HomeFragment.context, EventActivity::class.java))
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.tv_see_detail, R.id.iv_arrow -> {
                    if (ll_detail.visibility == View.GONE) {
                        ll_detail.expand(nested_scroll_view)
                        tv_see_detail.text = "닫기"
                        iv_arrow.setImageResource(R.drawable.arrow_up)
                    } else {
                        ll_detail.collapse()
                        tv_see_detail.text = "자세히보기"
                        iv_arrow.setImageResource(R.drawable.arrow_down)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBannerItems()
        viewModel.getGridItems()
    }


    override fun observeData() {
    }

}