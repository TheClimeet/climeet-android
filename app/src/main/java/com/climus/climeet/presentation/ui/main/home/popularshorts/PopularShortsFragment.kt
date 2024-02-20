package com.climus.climeet.presentation.ui.main.home.popularshorts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.climus.climeet.MainNavDirections
import com.climus.climeet.R
import com.climus.climeet.data.model.response.ShortsItem
import com.climus.climeet.databinding.FragmentBestClimerBinding
import com.climus.climeet.databinding.FragmentPopularShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.HomeViewModel
import com.climus.climeet.presentation.ui.main.home.model.HomeGym
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.home.popularshorts.adapter.PopularShortsAllRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.homegym.HomeGymRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.popularshorts.PopularShortsRVAdapter
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularShortsFragment : BaseFragment<FragmentPopularShortsBinding>(R.layout.fragment_popular_shorts) {

    private val viewModel: PopularShortsViewModel by viewModels()
    private var recyclerShorts: List<ShortsUiData> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getShorts()
        initEventObserve()
        setupOnClickListener()

    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->

                    uiState.shortsList?.let { shortsList ->
                        recyclerShorts = shortsList
                        setupPopularShortsList()
                    }

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