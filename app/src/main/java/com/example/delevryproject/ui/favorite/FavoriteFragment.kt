package com.example.delevryproject.ui.favorite

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.databinding.FragmentEatWhatBinding
import com.example.delevryproject.databinding.FragmentFavoriteBinding
import com.example.delevryproject.model.restaurant.RestaurantModel
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.eatwhat.EatWhatViewModel
import com.example.delevryproject.widget.adapter.ModelRecyclerAdapter
import com.example.delevryproject.widget.adapter.listener.restaurant.RestaurantLikeListListener
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class FavoriteFragment: BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>() {
    override val viewModel by viewModels<FavoriteViewModel>()

    override fun getViewBinding(): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater)

    private var isFirstShown = false

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        if (isFirstShown.not()) {
            isFirstShown = true
        } else {
            viewModel.fetchData()
        }
    }

    override fun observeData() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            checkListEmpty(it)
        }
    }

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, FavoriteViewModel>(listOf(), viewModel, adapterListener = object :
            RestaurantLikeListListener {

            override fun onDislikeItem(model: RestaurantModel) {
                viewModel.dislikeRestaurant(model.toEntity())
            }

            override fun onClickItem(model: RestaurantModel) {
//                startActivity(
//                    RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
//                )
            }
        })
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) {
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }
    companion object {

        fun newInstance() = FavoriteFragment()

        const val TAG = "FavoriteFragment"

    }


}