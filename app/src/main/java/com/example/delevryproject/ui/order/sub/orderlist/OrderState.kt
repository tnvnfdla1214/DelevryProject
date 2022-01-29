package com.example.delevryproject.ui.order.sub.orderlist

import android.net.Uri
import androidx.annotation.StringRes
import com.example.delevryproject.model.order.OrderModel

//로그인이 되어 있는지
//안되어 있다면 로그인을 해주세요
//되어 있는데 장바구니에 있다면 리스트 보여주기
//되어 있는데 장바구니에 없다면 없음 띄우기
sealed class OrderState {

    object Uninitialized: OrderState()

    object Loading: OrderState()

    data class Login(
        val idToken: String
    ): OrderState()

    sealed class Success: OrderState() {

        data class Registered(
            val orderList: List<OrderModel>
        ): Success()

        object NotRegistered: Success()

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): OrderState()

}
