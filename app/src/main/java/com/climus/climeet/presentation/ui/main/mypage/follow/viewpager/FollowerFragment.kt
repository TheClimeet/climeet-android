package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentFollowerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowerUiData
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowerUserRVAdapter
import com.climus.climeet.presentation.ui.toClimerProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment(val userId: Long) :
    BaseFragment<FragmentFollowerBinding>(R.layout.fragment_follower) {

    private val viewModel: FollowerViewModel by viewModels()
    private var userCategory: String = "Climber"
    private var recyclerUserFollower: List<UserFollowerUiData> = emptyList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        setupInitialSetting()
        viewModel.getUserFollowers(userId, userCategory)
        initStateObserve()
        initEventObserve()
    }

    private fun setupInitialSetting() {
        if (checkUserType())
            userCategory = "Manager"
    }

    private fun checkUserType(): Boolean {
        val userType = App.sharedPreferences.getString("X_MODE", "")
        return userType == "ADMIN"
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is FollowerEvent.NavigateToClimerProfile -> {
                        findNavController().toClimerProfile(it.id)

                    }
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.followerList.let { followerList ->

                        recyclerUserFollower = followerList
                        setupUserFollowerList()

                    }
                }
            }
        }
    }

    private fun setupUserFollowerList() {
        val followingRVAdapter = FollowerUserRVAdapter(recyclerUserFollower)
        setupRecyclerView(
            binding.rvSearchFollowing,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
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