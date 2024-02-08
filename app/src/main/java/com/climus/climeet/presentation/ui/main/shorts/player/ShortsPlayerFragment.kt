package com.climus.climeet.presentation.ui.main.shorts.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsPlayerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.ShortsViewModel
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailAdapter

class ShortsPlayerFragment: BaseFragment<FragmentShortsPlayerBinding>(R.layout.fragment_shorts_player) {

    private val sharedViewModel: ShortsViewModel by activityViewModels()
    private lateinit var adapter : ShortsDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
    }

    private fun setViewPager(){
        binding.vpShorts.adapter = ShortsDetailAdapter(requireActivity())
        setData()
    }

    private fun setData(){
        adapter.setData(sharedViewModel.uiState.value.shortsList)
    }

}