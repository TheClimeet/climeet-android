package com.climus.climeet.presentation.ui.main.shorts.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsPlayerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.ShortsViewModel
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsPlayerFragment: BaseFragment<FragmentShortsPlayerBinding>(R.layout.fragment_shorts_player) {

    private val sharedViewModel: ShortsViewModel by activityViewModels()
    private var adapter : ShortsDetailAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShortsDetailAdapter(this)
        setViewPager()
    }

    private fun setViewPager(){
        binding.vpShorts.adapter = adapter
        setData()
    }

    private fun setData(){
        adapter?.setData(sharedViewModel.uiState.value.shortsList)
    }

}