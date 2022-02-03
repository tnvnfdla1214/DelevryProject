package com.example.delevryproject.ui.home.restaurant.detail.review

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.delevryproject.databinding.FragmentListBinding
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.home.restaurant.detail.menu.RestaurantMenuListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewListFragment : BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)
    override val viewModel by viewModels<RestaurantReviewListViewModel>()

    override fun observeData() { }


    companion object {

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(restaurantTitle: String): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }

    }

}
