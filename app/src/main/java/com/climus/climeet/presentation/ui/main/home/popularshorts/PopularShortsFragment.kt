package com.climus.climeet.presentation.ui.main.home.popularshorts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.MainNavDirections
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentPopularShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.popularshorts.adapter.PopularShortsAllRVAdapter
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsThumbnailUiData
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularShortsFragment :
    BaseFragment<FragmentPopularShortsBinding>(R.layout.fragment_popular_shorts) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private var recyclerShorts: List<ShortsThumbnailUiData> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.initViewModel()
        sharedViewModel.getShorts(ShortsOption.NEW_SORT)
        initEventObserve()
        setupOnClickListener()

    }

    private fun initEventObserve() {

        repeatOnStarted {
            sharedViewModel.uiState.collect {
                if (it.shortsThumbnailList.isNotEmpty()) {
                    recyclerShorts = it.shortsThumbnailList
                    setupPopularShortsList()
                }
            }
        }

        repeatOnStarted {
            sharedViewModel.event.collect {
                when (it) {
                    is ShortsPlayerEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is ShortsPlayerEvent.NavigateToShortsPlayer -> findNavController().toShortsPlayer(
                        it.shortsId,
                        it.position
                    )

                }
            }
        }
    }

    private fun setupOnClickListener() {
        binding.icPopularShortsBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.icPopularShortsSearch.setOnClickListener {
            navigateToSearchCrag()
        }
    }

    private fun navigateToSearchCrag() {
        val action = MainNavDirections.actionHomeFragmentToSearchCragFragment()
        findNavController().navigate(action)
    }

    private fun setupPopularShortsList() {
        val popularShortsAllRVAdapter = PopularShortsAllRVAdapter(recyclerShorts)
        setupRecyclerView(binding.rvPopularShorts, popularShortsAllRVAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        val spanCount = 3
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), spanCount)
    }

}