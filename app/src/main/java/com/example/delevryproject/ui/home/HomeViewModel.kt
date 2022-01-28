package com.example.delevryproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.model.BannerItem
import com.example.delevryproject.data.model.GridItem
import com.example.delevryproject.data.repository.a_home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _bannerItemList: MutableLiveData<List<BannerItem>> = MutableLiveData()
    private val _gridItemList: MutableLiveData<List<GridItem>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()

    val bannerItemList: LiveData<List<BannerItem>>
        get() = _bannerItemList
    val gridItemList: LiveData<List<GridItem>>
        get() = _gridItemList
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    init {
        _currentPosition.value = 0
    }

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun getCurrentPosition() = currentPosition.value

    fun getBannerItems() {
        viewModelScope.launch {
            val bannerItemLiveData = homeRepository.getBannerItems()
            withContext(Main) {
                _bannerItemList.value = bannerItemLiveData
            }
        }
    }

    fun getGridItems() {
        viewModelScope.launch {
            val gridItemLiveData = homeRepository.getGridItems()
            withContext(Main) {
                _gridItemList.value = gridItemLiveData
            }
        }
    }

}