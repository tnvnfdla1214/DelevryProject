package com.example.delevryproject.ui.eatwhat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delevryproject.data.model.WhatToEat
import com.example.delevryproject.data.repository.b_eathome.EatWhatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class EatWhatViewModel
@Inject constructor(
    private val eatWhatRepository: EatWhatRepository
): ViewModel() {
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