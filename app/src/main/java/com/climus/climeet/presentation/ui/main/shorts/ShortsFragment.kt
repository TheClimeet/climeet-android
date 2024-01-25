package com.climus.climeet.presentation.ui.main.shorts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.UpdatedFollowAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsFragment : BaseFragment<FragmentShortsBinding>(R.layout.fragment_shorts) {

    private val viewModel: ShortsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.rvShortsThumbnail.adapter = ShortsThumbnailAdapter()
        binding.rvUpdatedFollow.adapter = UpdatedFollowAdapter()

        viewModel.getData()
    }


}