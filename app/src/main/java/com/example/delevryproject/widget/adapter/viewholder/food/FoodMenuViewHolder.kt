package com.example.delevryproject.widget.adapter.viewholder.food


import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.delevryproject.R
import com.example.delevryproject.databinding.ViewholderFoodMenuBinding
import com.example.delevryproject.extension.clear
import com.example.delevryproject.extension.load
import com.example.delevryproject.model.restaurant.FoodModel
import com.example.delevryproject.ui.base.BaseViewModel
import com.example.delevryproject.util.provider.ResourcesProvider
import com.example.delevryproject.widget.adapter.listener.AdapterListener
import com.example.delevryproject.widget.adapter.listener.restaurant.FoodMenuListListener
import com.example.delevryproject.widget.adapter.viewholder.ModelViewHolder

class FoodMenuViewHolder(
    private val binding: ViewholderFoodMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        foodImage.clear()
    }

    /* 1-2-2-1. 메뉴 나열 : 클릭하면 장바구니에 담기
    * 메뉴 정보 표시
    */
    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding) {
            foodImage.load(model.imageUrl, 24f, CenterCrop())
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourcesProvider.getString(R.string.price, model.price)
        }
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if (adapterListener is FoodMenuListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

}
