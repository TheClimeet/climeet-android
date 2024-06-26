package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentFollowingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.searchprofile.adapter.FollowingRVAdapter
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowGymRVAdapter
import com.climus.climeet.presentation.ui.toClimerProfile
import com.climus.climeet.presentation.ui.toGymProfile
import com.climus.climeet.presentation.util.Constants
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment(val userId: Long) :
    BaseFragment<FragmentFollowingBinding>(R.layout.fragment_following) {

    private val viewModel: FollowingViewModel by viewModels()

    private var recyclerClimber: List<UserFollowingUiData> = emptyList()
    private var recyclerGymFollowing: List<UserFollowingUiData> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getClimberFollowing()
        viewModel.getHomeGyms()
        initEventObserve()
        initStateObserve()
        tabListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is FollowingEvent.NavigateToClimerProfile -> findNavController().toClimerProfile(
                        it.id
                    )

                    is FollowingEvent.NavigateToGymProfile -> findNavController().toGymProfile(
                        it.id
                    )

                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.followingList.let { followingList ->

                        recyclerClimber = followingList
                        Log.d("recycler", recyclerClimber.toString())
                        setupFollowingList()

                    }
                    uiState.homegymList.let { homeGymList ->
                        recyclerGymFollowing = homeGymList
                        Log.d("recycler", recyclerGymFollowing.toString())
                        setupFollowingGymList()

                    }
                }
            }
        }
    }

    private fun setupFollowingList() {
        val followingRVAdapter = FollowingRVAdapter(recyclerClimber)
        setupRecyclerView(
            binding.rvSearchFollowing,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun setupFollowingGymList() {
        val followingRVAdapter = FollowGymRVAdapter(recyclerGymFollowing, ::navToGymProfile)
        setupRecyclerView(
            binding.rvFollowSearchCrags,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun navToGymProfile(gymId: Long) {

        App.sharedPreferences.edit().putLong("gymId", gymId)
            .apply()
        Log.d("gym_profile", "홈에서 암장 아이디 : $gymId")

        val access = App.sharedPreferences.getString(Constants.X_MODE, null)

        findNavController().toGymProfile(gymId)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        orientation: Int,
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }

    private fun tabListener() {
        binding.tlMode.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "암장" -> {
                        viewModel.changeMode(true)
                        viewModel.getHomeGyms()
                        binding.rvFollowSearchCrags.visibility = View.VISIBLE
                        binding.rvSearchFollowing.visibility = View.INVISIBLE

                    }

                    "클라이머" -> {
                        viewModel.changeMode(false)
                        viewModel.getClimberFollowing()
                        binding.rvFollowSearchCrags.visibility = View.INVISIBLE
                        binding.rvSearchFollowing.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

}