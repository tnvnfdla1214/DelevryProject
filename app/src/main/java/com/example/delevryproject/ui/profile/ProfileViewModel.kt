package com.example.delevryproject.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.local.preference.AppPreferenceManager
import com.example.delevryproject.data.repository.user.UserRepository
import com.example.delevryproject.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager): BaseViewModel() {

    val myStateLiveData = MutableLiveData<ProfileState>(ProfileState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLiveData.value = ProfileState.Loading
        appPreferenceManager.getIdToken()?.let {
            myStateLiveData.value = ProfileState.Login(it)
        } ?: kotlin.run {
            myStateLiveData.value = ProfileState.Success.NotRegistered
        }
    }
    //로그인 후 사용자 토큰 정보를 저장
    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    //유저 정보 설정 이름,사진
    @Suppress("UNCHECKED_CAST")
    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            myStateLiveData.value = ProfileState.Success.Registered(
                user.displayName ?: "익명",
                user.photoUrl)
        } ?: kotlin.run {
            myStateLiveData.value = ProfileState.Success.NotRegistered
        }
    }

}