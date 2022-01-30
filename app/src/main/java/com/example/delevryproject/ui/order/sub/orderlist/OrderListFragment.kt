package com.example.delevryproject.ui.order.sub.orderlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.delevryproject.databinding.FragmentBMartBinding
import com.example.delevryproject.databinding.FragmentOrderBinding
import com.example.delevryproject.databinding.FragmentOrderListBinding
import com.example.delevryproject.model.order.OrderModel
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.order.sub.bmart.BMartViewModel
import com.example.delevryproject.ui.profile.ProfileState
import com.example.delevryproject.widget.adapter.ModelRecyclerAdapter
import com.example.delevryproject.widget.adapter.listener.order.OrderListListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_order_list.*
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class OrderListFragment : BaseFragment<OrderListViewModel, FragmentOrderListBinding>() {
    override val viewModel by viewModels<OrderListViewModel>()
    override fun getViewBinding(): FragmentOrderListBinding = FragmentOrderListBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        Log.d("민규2","1")
        recyclerView.adapter = adapter
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

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
        Log.d("민규2","2")
        progressBar.isVisible = true
    }

    private fun handleLoginState(state: OrderState.Login) = with(binding) {
        Log.d("민규2","3")
        text_login.isGone = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //여기에 text 없어지면 될듯
                    val user = firebaseAuth.currentUser
                    Log.d("민규2", user.toString())
                    viewModel.setUserInfo(user)
                }
            }
    }
    private fun handleSuccessState(state: OrderState.Success) = with(binding) {
        Log.d("민규2","4")
        progressBar.isGone = true
        when (state) {
            is OrderState.Success.Registered -> {
                Log.d("민규2","5")
                handleRegisteredState(state)
            }
            is OrderState.Success.NotRegistered -> {
                Log.d("민규2","6")
            }
        }
    }
    private fun handleErrorState(state: OrderState.Error) {
        Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
    }

    private fun handleRegisteredState(state: OrderState.Success.Registered) = with(binding) {

        if (state.orderList.isEmpty()) {
            ggim.isGone = false
            recyclerView.isGone = true
        } else {
            ggim.isGone = true
            recyclerView.isGone = false
            adapter.submitList(state.orderList)
        }
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
}