package com.example.delevryproject.widget.adapter.viewholder.restaurant

import com.example.delevryproject.R
import com.example.delevryproject.databinding.ViewholderRestaurantBinding
import com.example.delevryproject.extension.clear
import com.example.delevryproject.extension.load
import com.example.delevryproject.model.restaurant.RestaurantModel
import com.example.delevryproject.ui.base.BaseViewModel
import com.example.delevryproject.util.provider.ResourcesProvider
import com.example.delevryproject.widget.adapter.listener.AdapterListener
import com.example.delevryproject.widget.adapter.listener.restaurant.RestaurantListListener
import com.example.delevryproject.widget.adapter.viewholder.ModelViewHolder


class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    /* 1-2. 가게 나열 : 클릭하면 "가게 상세" 이동 및 가게 필터링
    *  가게 정보 표시
    */
    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text = resourcesProvider.getString(R.string.review_count, model.reviewCount)
            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text = resourcesProvider.getString(R.string.delivery_time, minTime, maxTime)

            val (minTip, maxTip) = model.deliveryTipRange
            deliveryTipText.text = resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)
        }
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is RestaurantListListener) {
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

}
