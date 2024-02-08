package com.climus.climeet.presentation.ui.main.shorts.player

import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsDetailBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShortsDetailFragment @Inject constructor(
    private val data: ShortsUiData
): BaseFragment<FragmentShortsDetailBinding>(R.layout.fragment_shorts_detail) {


}