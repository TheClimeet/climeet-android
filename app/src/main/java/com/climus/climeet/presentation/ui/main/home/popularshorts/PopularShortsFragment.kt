package com.climus.climeet.presentation.ui.main.home.popularshorts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.climus.climeet.MainNavDirections
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentBestClimerBinding
import com.climus.climeet.databinding.FragmentPopularShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.model.HomeGym
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.home.popularshorts.adapter.PopularShortsAllRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.homegym.HomeGymRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.popularshorts.PopularShortsRVAdapter

class PopularShortsFragment : BaseFragment<FragmentPopularShortsBinding>(R.layout.fragment_popular_shorts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPopularShortsList()
        setupOnClickListener()

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
        val shortsAllList = arrayListOf(
            PopularShorts(null, "피커스 구로", "#000000", "V10", "#FFBEDF22"),
            PopularShorts(null, "더클라임 양재", "#FFFFFF", "V1", "#FFBEDF22"),
            PopularShorts(null, "피커스 사당", "#000000", "V9", "#FFBEDF22"),
            PopularShorts(null, "더클라임 마포", "#000000", "V3", "#FFBEDF22"),
            PopularShorts(null, "피커스 부평", "#FFFFFF", "V7", "#000FFF"),
            PopularShorts(null, "더클라임 부천", "#000000", "V2", "#000000"),
            PopularShorts(null, "피커스 구로", "#000000", "V10", "#FFBEDF22"),
            PopularShorts(null, "더클라임 양재", "#FFFFFF", "V1", "#FFBEDF22"),
            PopularShorts(null, "피커스 사당", "#000000", "V9", "#FFBEDF22"),
            PopularShorts(null, "더클라임 마포", "#000000", "V3", "#FFBEDF22"),
            PopularShorts(null, "피커스 부평", "#FFFFFF", "V7", "#000FFF"),
            PopularShorts(null, "더클라임 부천", "#000000", "V2", "#000000"),
            PopularShorts(null, "피커스 구로", "#000000", "V10", "#FFBEDF22"),
            PopularShorts(null, "더클라임 양재", "#FFFFFF", "V1", "#FFBEDF22"),
            PopularShorts(null, "피커스 사당", "#000000", "V9", "#FFBEDF22"),
            PopularShorts(null, "더클라임 마포", "#000000", "V3", "#FFBEDF22"),
            PopularShorts(null, "피커스 부평", "#FFFFFF", "V7", "#000FFF"),
            PopularShorts(null, "더클라임 부천", "#000000", "V2", "#000000")
        )

        val popularShortsAllRVAdapter = PopularShortsAllRVAdapter(shortsAllList)
        setupRecyclerView(binding.rvPopularShorts, popularShortsAllRVAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        val spanCount = 3
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), spanCount)
    }

}