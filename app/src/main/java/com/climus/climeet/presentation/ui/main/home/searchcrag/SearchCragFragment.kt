package com.climus.climeet.presentation.ui.main.home.searchcrag

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.databinding.FragmentSearchCragBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.FollowCragEvent
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import com.climus.climeet.presentation.ui.main.home.HomeFragmentDirections
import com.climus.climeet.presentation.ui.main.home.HomeViewModel
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.ui.main.home.recycler.popularroute.PopularRouteRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class SearchCragFragment : BaseFragment<FragmentSearchCragBinding>(R.layout.fragment_search_crag) {

    private val viewModel: SearchCragViewModel by viewModels()
    private var recyclerRoute: List<BestRouteDetailInfoResponse> = emptyList()
    private var adapter: FollowCragRVAdapter? = null
    private var isCrag: Boolean = true
    lateinit var editText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        adapter = FollowCragRVAdapter()
        binding.rvFollowSearchCrags.adapter = adapter
        viewModel.getRouteRankingOrderSelectionCount()

        initStateObserve()
        setupSearchCragTask()
        setupOnClickListener()

    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.routeList?.let { routeList ->
                        recyclerRoute = routeList
                        setupPopularRoutes()
                    }
                }
            }

            viewModel.uiState.collect {
                adapter?.setList(it.searchList, viewModel.keyword.value)
            }

        }

    }

    private fun setupSearchCragTask() {
        editText = binding.etSearchCrag
        editText.hint = "암장 검색하기"
        binding.clSerachCrags.visibility = View.VISIBLE

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clSerachCrags.visibility = if (charSequence.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                binding.rvFollowSearchCrags.visibility = if (charSequence.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                binding.rvFollowCrags.visibility = if (charSequence.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        if (binding.rvSearchCragRoutes1.visibility == View.VISIBLE) {
            binding.icEmptySearch.visibility = View.INVISIBLE
            binding.tvEmptySearch.visibility = View.INVISIBLE
        }
    }

    private fun setupSearchClimberTask() {
        editText = binding.etSearchCrag
        editText.hint = "클라이머 검색하기"
        binding.clSerachCrags.visibility = View.INVISIBLE
        binding.icEmptySearch.visibility = View.VISIBLE
        binding.tvEmptySearch.visibility = View.VISIBLE

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                //binding.rvFollowSearchCrags.visibility = if (charSequence.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

    }

    private fun setupOnClickListener() {

        binding.icPopularRoutesBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvSearchMenuCrag.setOnClickListener {
            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_greyfill_nostroke_10radius)
            binding.tvSearchMenuCrag.setTextColor(Color.WHITE)
            binding.tvSearchMenuClimer.setBackgroundResource(R.drawable.rect_blackfill_nostroke_10radius)
            val color: Int = Color.parseColor("#B3B3B3")
            binding.tvSearchMenuClimer.setTextColor(color)
            isCrag = true
            setupSearchCragTask()
        }

        binding.tvSearchMenuClimer.setOnClickListener {
            binding.tvSearchMenuClimer.setBackgroundResource(R.drawable.rect_greyfill_nostroke_10radius)
            binding.tvSearchMenuClimer.setTextColor(Color.WHITE)
            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_blackfill_nostroke_10radius)
            val color: Int = Color.parseColor("#B3B3B3")
            binding.tvSearchMenuCrag.setTextColor(color)
            isCrag = false
            setupSearchClimberTask()
        }
    }

    private fun setupPopularRoutes() {
        val popularRouteRVAdapter = PopularRouteRVAdapter(recyclerRoute)
        setupRecyclerView(binding.rvSearchCragRoutes1, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(binding.rvSearchCragRoutes2, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }

}