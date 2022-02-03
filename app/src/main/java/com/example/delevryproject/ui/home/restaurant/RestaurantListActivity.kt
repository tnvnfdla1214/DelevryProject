package com.example.delevryproject.ui.home.restaurant

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.delevryproject.R
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.databinding.ActivityEventBinding
import com.example.delevryproject.databinding.ActivityRestaurantlistBinding
import com.example.delevryproject.databinding.FragmentOrderBinding
import com.example.delevryproject.ui.base.BaseActivity
import com.example.delevryproject.ui.home.HomeViewModel
import com.example.delevryproject.ui.home.event.EventActivity
import com.example.delevryproject.ui.home.restaurant.fragment.RestaurantListFragment
import com.example.delevryproject.ui.main.MainViewModel
import com.example.delevryproject.ui.order.OrderViewModel
import com.example.delevryproject.ui.order.sub.bmart.BMartFragment
import com.example.delevryproject.ui.order.sub.orderlist.OrderListFragment
import com.example.delevryproject.ui.order.sub.shoppinglive.ShoppingLiveFragment
import com.example.delevryproject.widget.adapter.RestaurantListPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListActivity : BaseActivity<RestaurantListViewModel, ActivityRestaurantlistBinding>() {

    private lateinit var viewPagerAdapter: RestaurantListPagerAdapter

    override val viewModel by viewModels<RestaurantListViewModel>()

    override fun getViewBinding(): ActivityRestaurantlistBinding = ActivityRestaurantlistBinding.inflate(layoutInflater)

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() } //장바구니용

    override fun initViews() = with(binding) {

        val mapSearchInfoEntity = intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY) as MapSearchInfoEntity

        initViewPager(mapSearchInfoEntity.locationLatLng)

        /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
        *  필터링 칩을 클릭하여 가게 나열 방식 변경
        */
        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.isGone = true
                    changeRestaurantFilterOrder(RestautantFilterOrder.DEFAULT)
                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true
                }
                R.id.chipDeliveryTip -> {
                    chipInitialize.isVisible = true
                    changeRestaurantFilterOrder(RestautantFilterOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery -> {
                    chipInitialize.isVisible = true
                    changeRestaurantFilterOrder(RestautantFilterOrder.FAST_DELIVERY)
                }
                R.id.chipTopRate -> {
                    chipInitialize.isVisible = true
                    changeRestaurantFilterOrder(RestautantFilterOrder.TOP_RATE)
                }
            }
        }
    }


    /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
    *  클릭한 칩 방식으로 가게 나열 방식 변경
    */
    private fun changeRestaurantFilterOrder(order: RestautantFilterOrder) {
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantFilterOrder(order)
        }
    }


    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {

        /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
        *  카테고리를 받아와서, 그에 따라 위치기반의 가게 정보를 나열한다.
        */
        //위치 정보가 잘 확보된 상태여야 탭레이아웃 보여주겠다.
        orderChipGroup.isVisible = true
        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantCategories = RestaurantCategory.values()
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLng)
            }
            viewPagerAdapter = RestaurantListPagerAdapter(
                this@RestaurantListActivity,
                restaurantListFragmentList,
                locationLatLng
            )
            viewPager.adapter = viewPagerAdapter
            // 뷰페이저 재사용
            viewPager.offscreenPageLimit = restaurantCategories.size
            //텝레이아웃에 탭들을 뿌려주도록함
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(RestaurantCategory.values()[position].categoryNameId)
            }.attach()
        }
        //현재 위치정보랑 뷰페이저에 있는 위치정보가 맞지 않다면 재설정
        if (locationLatLng != viewPagerAdapter.locationLatLng) {
            viewPagerAdapter.locationLatLng = locationLatLng
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLng)
            }
        }
    }

//    override fun observeData() {
//        // 로그인면 내 화면 탭으로 이동, 비어있으면 장바구니 표시 X
//        viewModel.foodMenuBasketLiveData.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                binding.basketButtonContainer.isVisible = true
//                binding.basketCountTextView.text = getString(R.string.basket_count, it.size)
//                binding.basketButton.setOnClickListener {
//                    if (firebaseAuth.currentUser == null) {
//                        alertLoginNeed {
//                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
//                        }
//                    } else {
//                        startActivity(
//                            OrderMenuListActivity.newIntent(requireActivity())
//                        )
//                    }
//                }
//            } else {
//                binding.basketButtonContainer.isGone = true
//                binding.basketButton.setOnClickListener(null)
//            }
//        }
//
//    }

    override fun onResume() {
        super.onResume()
        //viewModel.checkMyBasket()
    }

    override fun observeData() { }

    companion object {

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, RestaurantListActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }

    }

}