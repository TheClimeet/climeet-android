package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFollowingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.mypage.follow.model.FollowingUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowingClimberRVAdapter
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowingGymRVAdapter
import com.climus.climeet.presentation.ui.toClimerProfile
import com.climus.climeet.presentation.ui.toGymProfile
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment(val userId: Long) :
    BaseFragment<FragmentFollowingBinding>(R.layout.fragment_following) {

    private val viewModel: FollowingViewModel by viewModels()

    private var recyclerClimber: MutableList<FollowingUiData> = mutableListOf()
    private var recyclerGym: MutableList<FollowingUiData> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getUserFollowing("Manager")

        initEventObserve()
        initStateObserve()
        tabListener()
    }

    private fun setupFollowingList() {
        val followingRVAdapter = FollowingClimberRVAdapter(recyclerClimber)
        setupRecyclerView(
            binding.rvClimberFollowing,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun setupFollowingGymList() {
        val followingRVAdapter = FollowingGymRVAdapter(recyclerGym)
        setupRecyclerView(
            binding.rvGymFollowing,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is FollowingEvent.NavigateToClimerProfile -> findNavController().toClimerProfile(it.id)
                    is FollowingEvent.NavigateToGymProfile -> findNavController().toGymProfile(it.id)
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.climberFollowingList.let { followingList ->
                        recyclerClimber = followingList.toMutableList()
                        Log.d("recycler", recyclerClimber.toString())

                        setupFollowingList()
                    }
                    uiState.gymFollowingList.let { homeGymList ->
                        recyclerGym = homeGymList.toMutableList()
                        Log.d("recycler", recyclerGym.toString())

                        setupFollowingGymList()
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
                        viewModel.getUserFollowing("Manager")
                        binding.rvGymFollowing.visibility = View.VISIBLE
                        binding.rvClimberFollowing.visibility = View.INVISIBLE

                    }

                    "클라이머" -> {
                        viewModel.changeMode(false)
                        viewModel.getUserFollowing("Climber")
                        binding.rvGymFollowing.visibility = View.INVISIBLE
                        binding.rvClimberFollowing.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun navToGymProfile(gymId: Long) {
        findNavController().toGymProfile(gymId)
    }

    private fun navToProfile(userId: Long) {
        findNavController().toClimerProfile(userId)
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