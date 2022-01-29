package com.example.delevryproject.ui.eatwhat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.local.model.WhatToEat
import com.example.delevryproject.data.repository.eathome.EatWhatRepository
import com.example.delevryproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class EatWhatViewModel
@Inject constructor(
    private val eatWhatRepository: EatWhatRepository
): BaseViewModel() {
    private val _eatWhatToEatList: MutableLiveData<List<WhatToEat>> = MutableLiveData()
    val eatWhatToEatList: LiveData<List<WhatToEat>>
        get() = _eatWhatToEatList

    fun getFakeWhatToEatList() {
        viewModelScope.launch {
            withContext(IO) {
                _eatWhatToEatList.postValue(eatWhatRepository.getWhatToEatItems())
            }
        }
    }
}