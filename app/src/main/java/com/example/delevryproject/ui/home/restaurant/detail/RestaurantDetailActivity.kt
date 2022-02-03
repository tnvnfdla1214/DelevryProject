package com.example.delevryproject.ui.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.delevryproject.R
import com.example.delevryproject.data.entitiy.locaion.MapSearchInfoEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantEntity
import com.example.delevryproject.data.entitiy.restaurant.RestaurantFoodEntity
import com.example.delevryproject.databinding.ActivityRestaurantDetailBinding
import com.example.delevryproject.extension.AppBarLayout
import com.example.delevryproject.extension.fromDpToPx
import com.example.delevryproject.extension.load
import com.example.delevryproject.ui.base.BaseActivity
import com.example.delevryproject.ui.home.HomeViewModel
import com.example.delevryproject.ui.home.mylocation.MyLocationViewModel
import com.example.delevryproject.ui.home.order.OrderMenuListActivity
import com.example.delevryproject.ui.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.example.delevryproject.ui.home.restaurant.detail.review.RestaurantReviewListFragment
import com.example.delevryproject.ui.home.restaurant.fragment.RestaurantListFragment
import com.example.delevryproject.util.event.MenuChangeEventBus
import com.example.delevryproject.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import com.google.android.material.appbar.AppBarLayout

import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantDetailActivity : BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    @Inject
    lateinit var restaurantdetailViewModelFactory: RestaurantDetailViewModel.RestaurantDetailViewModelFactory

    override val viewModel by viewModels<RestaurantDetailViewModel> {
        RestaurantDetailViewModel.provideFactory(
            restaurantdetailViewModelFactory,
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY) as RestaurantEntity
        )
    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding = ActivityRestaurantDetailBinding.inflate(layoutInflater)

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun initViews() {
        initAppBar()
    }

    private lateinit var viewPagerAdapter: RestaurantDetailListFragmentPagerAdapter

    private fun initAppBar() = with(binding) {
        //스크롤을 내리면 tolbar에 제목이 작성되는 기능 올리면 사라짐
        appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val topPadding = 300f.fromDpToPx().toFloat()
                val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
                val abstractOffset = abs(verticalOffset)

                val realAlphaVerticalOffset: Float = if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

                if (abstractOffset < topPadding) {
                    restaurantTitleTextView.alpha = 0f
                    return@OnOffsetChangedListener
                }
                val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
                restaurantTitleTextView.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
            })

        /* 1-2-2. 가게 상세 : 전화, 찜, 공유
        */
        toolbar.setNavigationOnClickListener {
            finish()
        }
        callButton.setOnClickListener {
            viewModel.getRestaurantPhoneNumber()?.let { telNumber ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                startActivity(intent)
            }
        }
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "맛있는 음식점 : ${restaurantInfo.restaurantTitle}" +
                            "\n평점 : ${restaurantInfo.grade}" +
                            "\n연락처 : ${restaurantInfo.restaurantTelNumber}"
                    )
                    Intent.createChooser(this, "친구에게 공유하기")
                }
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkMyBasket()
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            is RestaurantDetailState.Loading -> {
                handleLoading()
            }
            is RestaurantDetailState.Success -> {
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    /* 1-2-2. 가게 상세 : 전화, 찜, 공유
    * 가게 상세 정보 표시
    */
    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        ratingTextView.text = restaurantEntity.grade.toString()
        deliveryTimeText.text =
            getString(R.string.delivery_expected_time, restaurantEntity.deliveryTimeRange.first, restaurantEntity.deliveryTimeRange.second)
        deliveryTipText.text =
            getString(R.string.delivery_tip_range, restaurantEntity.deliveryTipRange.first, restaurantEntity.deliveryTipRange.second)

        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this@RestaurantDetailActivity, if (state.isLiked == true) {
                R.drawable.ic_heart_enable
            } else {
                R.drawable.ic_heart_disable
            }),
            null, null, null
        )

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(state.restaurantEntity.restaurantInfoId, state.restaurantEntity.restaurantTitle, state.restaurantFoodList)
        }

        notifyBasketCount(state.foodMenuListInBasket)

        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction

        if (isClearNeed) {
            alertClearNeedInBasket(afterAction)
        }
    }

    private fun initViewPager(restaurantInfoId: Long, restaurantTitle: String, restaurantFoodList: List<RestaurantFoodEntity>?) {
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                ),
            )
        )
        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.menuAndReviewTabLayout, binding.menuAndReviewViewPager) { tab, position ->
            tab.setText(RestaurantDetailCategory.values()[position].categoryNameId)
        }.attach()

    }

    /* 1-2-1. 장바구니
    * 장바구니의 메뉴 개수
    * 로그인이 필요하다.
    */
    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) = with(binding) {
        basketCountTextView.text = if (foodMenuListInBasket.isNullOrEmpty()) {
            "0"
        } else {
            getString(R.string.basket_count, foodMenuListInBasket.size)
        }
        basketButton.setOnClickListener {
            if (firebaseAuth.currentUser == null) {
                Toast.makeText(this@RestaurantDetailActivity, "로그인을 해주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (foodMenuListInBasket.isNullOrEmpty()) {
                    Toast.makeText(this@RestaurantDetailActivity, "장바구니에 주문할 메뉴를 추가해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                startActivity(
                    OrderMenuListActivity.newIntent(this@RestaurantDetailActivity)
                )
            }
        }
    }

    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("장바구니에는 같은 가게의 메뉴만 담을 수 있습니다.")
            .setMessage("선택하신 메뉴를 장바구니에 담을 경우 이전에 담은 메뉴가 삭제됩니다.")
            .setPositiveButton("담기") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) = Intent(context, RestaurantDetailActivity::class.java).apply {
            putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
        }
    }

}
