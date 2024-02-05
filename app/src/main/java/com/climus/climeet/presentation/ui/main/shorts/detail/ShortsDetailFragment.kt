package com.climus.climeet.presentation.ui.main.shorts.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsDetailBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.ShortsViewModel

class ShortsDetailFragment: BaseFragment<FragmentShortsDetailBinding>(R.layout.fragment_shorts_detail) {

    private val sharedViewModel: ShortsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}