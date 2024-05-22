package com.climus.climeet.presentation.ui.main.global.searchprofile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.databinding.FragmentSearchProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.searchprofile.adapter.FollowingCragInfoAdapter
import com.climus.climeet.presentation.ui.main.global.searchprofile.adapter.SearchProfileAdapter
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.UserFollowingUiData
import com.climus.climeet.presentation.ui.main.global.searchprofile.adapter.FollowingRVAdapter
import com.climus.climeet.presentation.ui.toClimerProfile
import com.climus.climeet.presentation.ui.toGymProfile
import com.climus.climeet.presentation.util.Constants
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchProfileFragment :
    BaseFragment<FragmentSearchProfileBinding>(R.layout.fragment_search_profile) {

    private val viewModel: SearchProfileViewModel by viewModels()
    private var adapter: SearchProfileAdapter? = null
    private var recyclerClimber: List<UserFollowingUiData> = emptyList()
    private var recyclerFollowingHomeGym: List<UserHomeGymDetailResponse> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        adapter = SearchProfileAdapter()
        binding.rvSearchResult.adapter = adapter
        viewModel.getGymFollowing()
        initEventObserve()
        initStateObserve()
        tabListener()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SearchProfileEvent.NavigateToClimerProfile -> findNavController().toClimerProfile(
                        it.id
                    )

                    is SearchProfileEvent.NavigateToGymProfile -> findNavController().toGymProfile(
                        it.id
                    )

                    is SearchProfileEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initStateObserve() {

        repeatOnStarted {
            viewModel.uiState.collect { uiState ->
                // 프로필 리스트 업데이트
                adapter?.setList(uiState.profileList, viewModel.keyword.value)
                Log.d("profileList", uiState.profileList.toString());

                uiState.followGymList?.let { followGymList ->
                    recyclerFollowingHomeGym = followGymList
                    setupFollowingHomeGymList()
                }

                uiState.followingList.let { followingList ->
                    Log.d("followingList ", followingList.toString())
                    recyclerClimber = followingList
                    setupFollowingList()

                }
            }
        }

    }

    private fun setupFollowingList() {
        val followingRVAdapter = FollowingRVAdapter(recyclerClimber)
        setupRecyclerView(
            binding.rvFollowingList,
            followingRVAdapter,
            LinearLayoutManager.VERTICAL
        )
    }

    private fun tabListener() {
        binding.tlMode.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "암장" -> {
                        viewModel.changeMode(true)
                        binding.etSearchCrag.hint = "암장 검색하기"
                        binding.tvSearchHomgym.visibility = View.VISIBLE
                        binding.rvFollowingList.visibility = View.INVISIBLE
                        binding.rvSearchFollowRoute.visibility = View.VISIBLE
                        viewModel.getGymFollowing()
                    }
                    "클라이머" -> {
                        viewModel.changeMode(false)
                        binding.etSearchCrag.hint = "클라이머 검색하기"
                        binding.tvSearchHomgym.visibility = View.GONE
                        binding.rvFollowingList.visibility = View.VISIBLE
                        binding.rvSearchFollowRoute.visibility = View.INVISIBLE
                        viewModel.getClimberFollowing()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setupFollowingHomeGymList() {
        val followingCragInfoAdapter = FollowingCragInfoAdapter(recyclerFollowingHomeGym, ::navToGymProfile)
        setupRecyclerView(
            binding.rvSearchFollowRoute,
            followingCragInfoAdapter,
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
        orientation: Int
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }
}