package com.climus.climeet.presentation.ui.main.home.search.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.databinding.FragmentSearchCragBinding
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import com.climus.climeet.presentation.ui.main.home.search.recycler.FollowGymRVAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchCragFragment : Fragment() {

    private lateinit var binding: FragmentSearchCragBinding
    private val viewModel: SearchCragViewModel by viewModels()
    private var recyclerGymFollowing: List<UserHomeGymDetailResponse> = emptyList()

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_crag, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGymFollowing()

        initStateObserve()
    }


    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.followGymList.let { followGymList ->

                        recyclerGymFollowing = followGymList
                        setupFollowingGymList()

                    }
                }
            }
        }
    }

    private fun setupFollowingGymList() {
        val homeGymRVAdapter = FollowGymRVAdapter(recyclerGymFollowing)
        setupRecyclerView(binding.rvSearchCragRoutes1, homeGymRVAdapter, LinearLayoutManager.VERTICAL)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }
}