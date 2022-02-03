package com.example.delevryproject.ui.home.restaurant.fragment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.databinding.FragmentListBinding
import com.example.delevryproject.model.restaurant.RestaurantModel
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.home.HomeViewModel
import com.example.delevryproject.ui.home.mylocation.MyLocationViewModel
import com.example.delevryproject.ui.home.restaurant.RestaurantCategory
import com.example.delevryproject.widget.adapter.ModelRecyclerAdapter
import com.example.delevryproject.widget.adapter.listener.restaurant.RestaurantListListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantListFragment : BaseFragment<RestaurantListFagmentViewModel, FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
    private val locationLatLngEntity by lazy<LocationLatLngEntity> { arguments?.getParcelable(LOCATION_KEY)!! }


    @Inject
    lateinit var locationViewModelFactory: RestaurantListFagmentViewModel.RestaurantListFagmentViewModelFactory

    override val viewModel by viewModels<RestaurantListFagmentViewModel> {
        RestaurantListFagmentViewModel.provideFactory(
            locationViewModelFactory,
            restaurantCategory,
            locationLatLngEntity
        )
    }



    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListFagmentViewModel>(listOf(), viewModel, adapterListener = object :
            RestaurantListListener {
            /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
            *  매장 리스트중에서 한 아이템이 클릭되었을 경우 "가게 상세"로 이동한다.
            */
            override fun onClickItem(model: RestaurantModel) {
//                startActivity(
//                    RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
//                )
            }
        })
    }

    override fun initViews() = with(binding) {
        recyclerVIew.adapter = adapter
    }

    override fun observeData() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    companion object {
        const val RESTAURANT_KEY = "Restaurant"
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"

        fun newInstance(restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity): RestaurantListFragment {
            val bundle = Bundle().apply {
                putSerializable(RESTAURANT_CATEGORY_KEY, restaurantCategory)
                putParcelable(LOCATION_KEY, locationLatLng)
            }

            return RestaurantListFragment().apply {
                arguments = bundle
            }
        }

    }
}