package com.climus.climeet.presentation.ui.main.home.popularroutes

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
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentPopularRoutesBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.home.popularroutes.adapter.PopularRoutesAllRVadapter
import com.climus.climeet.presentation.ui.main.home.popularshorts.adapter.PopularShortsAllRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.popularroute.PopularRouteRVAdapter

class PopularRoutesFragment : BaseFragment<FragmentPopularRoutesBinding>(R.layout.fragment_popular_routes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPopularRoutesList()
        setupOnClickListener()

    }

    private fun setupOnClickListener() {
        binding.icPopularRoutesBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupPopularRoutesList() {
        val routeList = arrayListOf(
            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
            PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
            PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
            PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
            PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
            PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루"),
            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
            PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
            PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
            PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
            PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
            PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루"),
            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
            PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
            PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
            PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
            PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
            PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루")
        )

        val popularRoutesAllRVAdapter = PopularRoutesAllRVadapter(routeList)
        setupRecyclerView(binding.rvPopularRoutes, popularRoutesAllRVAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        val spanCount = 3
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), spanCount)
    }

}