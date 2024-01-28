package com.climus.climeet.presentation.ui.main.home.searchcrag

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchCragBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.ui.main.home.recycler.popularroute.PopularRouteRVAdapter

class SearchCragFragment : BaseFragment<FragmentSearchCragBinding>(R.layout.fragment_search_crag) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListener()
        setupPopularRoutes()
        val editText = binding.etSearchCrag
        val clearIcon = binding.imageViewClear

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                clearIcon.visibility = if (charSequence.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                binding.tvSearchCragHomegym.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.ivSearchCragHomegymProfile1.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.ivSearchCragHomegymProfile2.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.tvSearchCragHomegymName1.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.tvSearchCragHomegymName2.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.rvSearchCragRoutes1.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.rvSearchCragRoutes2.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun setupOnClickListener() {
        binding.imageViewClear.setOnClickListener {
            binding.etSearchCrag.text.clear()
            binding.imageViewClear.visibility = View.INVISIBLE
        }

        binding.tvSearchMenuCrag.setOnClickListener {
            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_greyfill_nostroke_10radius)
            binding.tvSearchMenuCrag.setTextColor(Color.WHITE)
            binding.tvSearchMenuClimer.setBackgroundResource(R.drawable.rect_blackfill_nostroke_10radius)
            val color: Int = Color.parseColor("#B3B3B3")
            binding.tvSearchMenuClimer.setTextColor(color)
        }

        binding.tvSearchMenuClimer.setOnClickListener {
            binding.tvSearchMenuClimer.setBackgroundResource(R.drawable.rect_greyfill_nostroke_10radius)
            binding.tvSearchMenuClimer.setTextColor(Color.WHITE)
            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_blackfill_nostroke_10radius)
            val color: Int = Color.parseColor("#B3B3B3")
            binding.tvSearchMenuCrag.setTextColor(color)
        }
    }

    private fun setupPopularRoutes() {
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

        val popularRouteRVAdapter = PopularRouteRVAdapter(routeList)
        setupRecyclerView(binding.rvSearchCragRoutes1, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(binding.rvSearchCragRoutes2, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }
}