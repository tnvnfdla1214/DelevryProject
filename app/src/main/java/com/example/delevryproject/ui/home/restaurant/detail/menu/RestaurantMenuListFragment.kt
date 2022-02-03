package com.example.delevryproject.ui.home.restaurant.detail.menu

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity
import com.example.delevryproject.databinding.FragmentListBinding
import com.example.delevryproject.model.restaurant.FoodModel
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.home.restaurant.detail.RestaurantDetailViewModel
import com.example.delevryproject.widget.adapter.ModelRecyclerAdapter
import com.example.delevryproject.widget.adapter.listener.restaurant.FoodMenuListListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantMenuListFragment : BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    private val restaurantId by lazy { arguments?.getLong(RESTAURANT_ID_KEY, -1) }

    private val restaurantFoodList by lazy { arguments?.getParcelableArrayList<RestaurantFoodEntity>(FOOD_LIST_KEY)!! }

    @Inject
    lateinit var restaurantmenuListViewModelFactory: RestaurantMenuListViewModel.RestaurantMenuListViewModelFactory

    override val viewModel by viewModels<RestaurantMenuListViewModel> {
        RestaurantMenuListViewModel.provideFactory(
            restaurantmenuListViewModelFactory,
            restaurantId,
            restaurantFoodList
        )
    }

    //SharedViewModel ->hilt
    private val restaurantDetailViewModel by activityViewModels<RestaurantDetailViewModel>() //

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    /* 1-2-2-1. 메뉴 나열 : 클릭하면 장바구니에 담기
    * 클릭 시 장바구니에 메뉴 담기
    */
    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(listOf(), viewModel, adapterListener = object :
            FoodMenuListListener {
            override fun onClickItem(model: FoodModel) {
                viewModel.insertMenuInBasket(model)
            }
        })
    }

    override fun initViews() = with(binding) {
        recyclerVIew.adapter = adapter
    }

    override fun observeData() {
        viewModel.restaurantMenuListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.menuBasketLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "장바구니에 담겼습니다. 메뉴 : ${it.title}", Toast.LENGTH_SHORT).show()
            restaurantDetailViewModel.notifyFoodMenuListInBasket(it)
        }
        /* 1-2-2-1. 메뉴 나열 : 클릭하면 장바구니에 담기
        * restaurantDetailViewModel에 다른 가게 메뉴가 있는지 없는지 알려준다.
        */
        viewModel.isClearNeedInBasketLiveData.observe(viewLifecycleOwner) { (isClearNeed, afterAction) ->
            if (isClearNeed) {
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed, afterAction)
            }
        }
    }

    companion object {
        const val RESTAURANT_ID_KEY = "restaurantId"
        const val FOOD_LIST_KEY = "foodList"

        fun newInstance(restaurantId: Long, foodList: ArrayList<RestaurantFoodEntity>): RestaurantMenuListFragment {
            val bundle = Bundle().apply {
                putLong(RESTAURANT_ID_KEY, restaurantId)
                putParcelableArrayList(FOOD_LIST_KEY, foodList)
            }

            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }

    }

}
