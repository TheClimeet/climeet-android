package com.climus.climeet.presentation.ui.main.home.popularroutes

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
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.databinding.FragmentPopularRoutesBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.HomeViewModel
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.home.popularroutes.adapter.PopularRoutesAllRVadapter
import com.climus.climeet.presentation.ui.main.home.popularshorts.adapter.PopularShortsAllRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.popularroute.PopularRouteRVAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularRoutesFragment : BaseFragment<FragmentPopularRoutesBinding>(R.layout.fragment_popular_routes) {

    private val viewModel: PopularRoutesViewModel by viewModels()
    private var recyclerRoute: List<BestRouteDetailInfoResponse> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRouteRankingOrderSelectionCount()
        initEventObserve()
        setupOnClickListener()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->

                    uiState.routeList?.let { routeList ->
                        Log.d("RouteList", routeList.toString())
                        recyclerRoute = routeList
                        setupPopularRoutesList()
                    }
                }
            }
        }
    }

    private fun setupOnClickListener() {
        binding.icPopularRoutesBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.icPopularRoutesSearch.setOnClickListener {
            navigateToSearchCrag()
        }
    }

    private fun navigateToSearchCrag() {
        val action = MainNavDirections.actionHomeFragmentToSearchCragFragment()
        findNavController().navigate(action)
    }

    private fun setupPopularRoutesList() {
//        val routeList = arrayListOf(
//            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
//            PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
//            PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
//            PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
//            PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
//            PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루"),
//            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
//            PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
//            PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
//            PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
//            PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
//            PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루"),
//            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
//            PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
//            PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
//            PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
//            PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
//            PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루")
//        )

        val popularRoutesAllRVAdapter = PopularRoutesAllRVadapter(recyclerRoute)
        setupRecyclerView(binding.rvPopularRoutes, popularRoutesAllRVAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        val spanCount = 3
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), spanCount)
    }

}