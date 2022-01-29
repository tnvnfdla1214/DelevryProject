package com.example.delevryproject.ui.order.sub.orderlist

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.delevryproject.databinding.FragmentDeliverTakeOutBinding
import com.example.delevryproject.model.order.OrderModel
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.widget.adapter.ModelRecyclerAdapter
import com.example.delevryproject.widget.adapter.listener.order.OrderListListener
import com.google.firebase.auth.GoogleAuthProvider

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class OrderListFragment: BaseFragment<OrderListViewModel, FragmentDeliverTakeOutBinding>() {
    override val viewModel by viewModels<OrderListViewModel>()

    override fun getViewBinding(): FragmentDeliverTakeOutBinding = FragmentDeliverTakeOutBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }
    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel, OrderListViewModel>(listOf(), viewModel, adapterListener = object : OrderListListener {

            override fun writeRestaurantReview(orderId: String, restaurantTitle: String) {
//                startActivity(
//                    AddRestaurantReviewActivity.newIntent(requireContext(), orderId, restaurantTitle)
//                )
            }
        })
    }


    override fun observeData() = viewModel.myStateLiveData.observe(this) {
        when (it) {
            is OrderState.Uninitialized -> initViews()
            is OrderState.Loading -> handleLoadingState()
            is OrderState.Login -> handleLoginState(it)
            is OrderState.Success -> handleSuccessState(it)
            is OrderState.Error -> handleErrorState(it)
        }
    }
    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
    }
    private fun handleLoginState(state: OrderState.Login) = with(binding) {
        progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }


}