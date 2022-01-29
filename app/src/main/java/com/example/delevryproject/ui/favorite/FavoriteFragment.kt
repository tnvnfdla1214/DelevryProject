package com.example.delevryproject.ui.favorite

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.databinding.FragmentEatWhatBinding
import com.example.delevryproject.databinding.FragmentFavoriteBinding
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.eatwhat.EatWhatViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class FavoriteFragment: BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>() {
    override val viewModel by viewModels<FavoriteViewModel>()

    override fun getViewBinding(): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater)

    override fun observeData() {

    }

}