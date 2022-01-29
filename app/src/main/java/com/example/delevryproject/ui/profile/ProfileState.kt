package com.example.delevryproject.ui.profile

import android.net.Uri
import androidx.annotation.StringRes

sealed class ProfileState {

    object Uninitialized: ProfileState()

    object Loading: ProfileState()

    data class Login(
        val idToken: String
    ): ProfileState()

    sealed class Success: ProfileState() {

        data class Registered(
            val userName: String,
            val profileImageUri: Uri?, //나중에 추가하기
        ): Success()

        object NotRegistered: Success()

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): ProfileState()

}
