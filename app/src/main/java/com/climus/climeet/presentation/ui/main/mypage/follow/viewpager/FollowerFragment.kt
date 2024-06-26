package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFollowerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowerGymRVAdapter
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowerClimberRVAdapter
import com.climus.climeet.presentation.ui.toClimerProfile
import com.climus.climeet.presentation.ui.toGymProfile
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment(val userId: Long) :
    BaseFragment<FragmentFollowerBinding>(R.layout.fragment_follower) {

    private val viewModel: FollowerViewModel by viewModels()

    private var recyclerClimber: List<FollowUiData> = emptyList()
    private var recyclerGym: List<FollowUiData> = emptyList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getUserFollowers("Manager")

        initStateObserve()
        initEventObserve()
        tabListener()
    }

    private fun setupGymFollowerList() {
        val followingRVAdapter = FollowerGymRVAdapter(recyclerGym, viewModel)
        setupRecyclerView(
            binding.rvGymFollower,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun setupUserFollowerList() {
        val followingRVAdapter = FollowerClimberRVAdapter(recyclerClimber, viewModel)
        setupRecyclerView(
            binding.rvClimberFollower,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }
    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is FollowerEvent.NavigateToClimerProfile -> findNavController().toClimerProfile(it.userId)
                    is FollowerEvent.NavigateToGymProfile -> findNavController().toGymProfile(it.gymId)
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.climberFollowerList.let { followingList ->
                        recyclerClimber = followingList
                        Log.d("recycler", recyclerClimber.toString())

                        setupUserFollowerList()
                    }
                    uiState.gymFollowerList.let { homeGymList ->
                        recyclerGym = homeGymList
                        Log.d("recycler", recyclerGym.toString())

                        setupGymFollowerList()
                    }
                }
            }
        }
    }

    private fun tabListener() {
        binding.tlMode.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "암장" -> {
                        viewModel.changeMode(true)
                        viewModel.getUserFollowers("Manager")
                        binding.rvGymFollower.visibility = View.VISIBLE
                        binding.rvClimberFollower.visibility = View.INVISIBLE

                    }

                    "클라이머" -> {
                        viewModel.changeMode(false)
                        viewModel.getUserFollowers("Climber")
                        binding.rvGymFollower.visibility = View.INVISIBLE
                        binding.rvClimberFollower.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        orientation: Int,
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }

}