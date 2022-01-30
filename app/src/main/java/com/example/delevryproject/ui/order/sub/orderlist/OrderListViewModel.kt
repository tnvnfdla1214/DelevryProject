package com.example.delevryproject.ui.order.sub.orderlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.R
import com.example.delevryproject.data.entitiy.order.OrderEntity
import com.example.delevryproject.data.local.preference.AppPreferenceManager
import com.example.delevryproject.data.repository.order.OrderRepository
import com.example.delevryproject.data.repository.order.OrderRepositoryImpl
import com.example.delevryproject.data.repository.user.UserRepository
import com.example.delevryproject.model.order.OrderModel
import com.example.delevryproject.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val orderRepository: OrderRepository
): BaseViewModel() {

    val myStateLiveData = MutableLiveData<OrderState>(OrderState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLiveData.value = OrderState.Loading
        appPreferenceManager.getIdToken()?.let {
            myStateLiveData.value = OrderState.Login(it)
        } ?: kotlin.run {
            myStateLiveData.value = OrderState.Success.NotRegistered
        }
    }

    /* 3. 내정보 탭
      3-1. 구글 로그인 : 로그인 기록 없을 시 하게 함
    *  로그인 후 사용자 토큰 정보를 저장
    */
    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    /* 3. 내정보 탭
      3-1. 구글 로그인 : 로그인 기록 없을 시 하게 함
    *  유저 정보 설정
    *  UID 기반으로 getAllOrderMenus로 모든 주문 불러와서  Success / Error 구분
    */
    @Suppress("UNCHECKED_CAST")
    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            when (val orderMenusResult = orderRepository.getAllOrderMenus(user.uid)) {
                is OrderRepositoryImpl.Result.Success<*> -> {
                    val orderList: List<OrderEntity> = orderMenusResult.data as List<OrderEntity>
                    Log.e("orders", orderMenusResult.data.toString())
                    myStateLiveData.value = OrderState.Success.Registered(
                        orderList.map { order ->
                            OrderModel(
                                id = order.hashCode().toLong(),
                                orderId = order.id,
                                userId = order.userId,
                                restaurantId = order.restaurantId,
                                foodMenuList = order.foodMenuList,
                                restaurantTitle = order.restaurantTitle
                            )
                        }
                    )
                    Log.e("orders", orderList.toString())
                }
                is OrderRepositoryImpl.Result.Error -> {
                    myStateLiveData.value = OrderState.Error(
                        R.string.request_error,
                        orderMenusResult.e
                    )
                }
            }

        } ?: kotlin.run {
            myStateLiveData.value = OrderState.Success.NotRegistered
        }
    }

}
