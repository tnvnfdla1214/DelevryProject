package com.example.delevryproject.ui.home

/*
1. 현재위치 불러오기
2. 해당하는 메뉴에 따른 데이터 액티비티로 넘겨주기
3. 장바구니 동그란거 추가
 */

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.delevryproject.R
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.local.model.BannerItem
import com.example.delevryproject.databinding.FragmentHomeBinding
import com.example.delevryproject.ui.home.event.EventActivity
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.extension.collapse
import com.example.delevryproject.extension.expand
import com.example.delevryproject.ui.home.HomeViewModel.Companion.MY_LOCATION_KEY
import com.example.delevryproject.ui.home.mylocation.MyLocationActivity
import com.example.delevryproject.ui.home.restaurant.RestaurantListActivity

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), Interaction {

    override val viewModel by viewModels<HomeViewModel>()
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    // changeLocationLauncher가 쓰이는 곳에서 intent한 액티비티에서 전달 받은 값을 가지고 loadReverseGeoInformation를 한번 더함
    private val changeLocationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<MapSearchInfoEntity>(MY_LOCATION_KEY)?.let { myLocationInfo ->
                viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)
            }
        }
    }
    override fun initViews() = with(binding) {
        /* 1-1. 위치 정보 : 위치 정보 감지 및 재설정
        *  주소가 표시되어있는 텍스트 클릭하면 위치 재설정 가능
        */
        adressgroup.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(), mapInfo
                    )
                )
            }
        }

    }

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
            gridRecyclerViewAdapter = GridRecyclerViewAdapter(this@HomeFragment)
            layoutManager = GridLayoutManager(this@HomeFragment.context, 4)
            adapter = gridRecyclerViewAdapter

        }
    }
    //배너 광고
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

    //음식 리스트 보여주는거 -> 나중에 개별 리스트로 넘겨주는걸로 수정
    //Intent로 mapSearchInfoEntity 넘겨줘야 함
    //EventActivity -> 레스토랑 액티비티로 변경해야함
    override fun onGridItemClicked(itemView: View) {
        var Intent = Intent(this@HomeFragment.context, RestaurantListActivity::class.java)
        viewModel.getMapSearchInfo()?.let { mapInfo ->
            Intent = RestaurantListActivity.newIntent(
                requireContext(), mapInfo
            )
            startActivity(Intent)
        }
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

    /* 1-1. 위치 정보 : 위치 정보 감지 및 재설정
     * HomeViewModel로 위치정보 전송
    */
    inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            viewModel.loadReverseGeoInformation(
                // 별거 아닌거도 전부 entity를 만들어 사용
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            removeLocationListener()
        }
    }
    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    override fun observeData() {
        //HomeState에서 위치정보의 현재 state에 따라서 분기를 줘서 처리
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.Uninitialized -> {
                    /* 1-1. 위치 정보 : 위치 정보 감지 및 재설정
                    * 아직 초기화 되지 않았을 때
                    */
                    getMyLocation()
                }
                is HomeState.Loading -> {
                    binding.locationLoading.isVisible = true
                    binding.locationTitleTextView.text = getString(R.string.loading)
                }
                is HomeState.Success -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleTextView.text = it.mapSearchInfoEntity.fullAddress
                    if (it.isLocationSame.not()) { //나중에 이거 예쁘게 꾸미기
                        Toast.makeText(requireContext(), "위치가 맞는지 확인해주세요!", Toast.LENGTH_SHORT).show()
                    }
                }
                is HomeState.Error -> {
                    Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    private fun getMyLocation() {
        // location manager 설정
        if (::locationManager.isInitialized.not()) {
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        //GPS 켜져있는지 확인
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        /* 1-1. 위치 정보 : 위치 정보 감지 및 재설정
        * 위치 접근 권한 요청
        */
        if (isGpsEnable) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    /* 1-1. 위치 정보 : 위치 정보 감지 및 주변 가게
    * 위치 접근 권한 요청 런처
    */
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 받은 권한
            val responsePermissions = permissions.entries.filter {
                it.key == Manifest.permission.ACCESS_FINE_LOCATION
                        || it.key == Manifest.permission.ACCESS_COARSE_LOCATION
            }
            // 필요한 권한 개수 = 받은 권한 개수 인지 확인
            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleTextView) {
                    text = getString(R.string.please_request_location_permission)
                    setOnClickListener {
                        /* 1-1. 위치 정보 : 위치 정보 감지 및 재설정
                        * 권한 허용 후 위치정보를 가져온다.
                        */
                        getMyLocation()
                    }
                }
                Toast.makeText(requireContext(), getString(R.string.can_not_assigned_permission), Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }


    companion object {

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        const val TAG = "HomeFragment"

        fun newInstance() = HomeFragment()
    }

}