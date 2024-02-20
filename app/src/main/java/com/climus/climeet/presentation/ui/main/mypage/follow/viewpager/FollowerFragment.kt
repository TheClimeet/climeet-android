package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.graphics.Color
import android.os.Bundle
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
import com.climus.climeet.app.App
import com.climus.climeet.data.model.response.UserFollowingInfoResponse
import com.climus.climeet.databinding.FragmentFollowerBinding
import com.climus.climeet.presentation.ui.main.home.recycler.following.FollowingRVAdapter
import com.climus.climeet.presentation.ui.main.mypage.follow.viewpager.adapter.FollowerUserRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private lateinit var binding : FragmentFollowerBinding
    private val viewModel: FollowerViewModel by viewModels()
    var userCategory: String = "Climber"
    private var recyclerUserFollower: List<UserFollowingInfoResponse> = emptyList()

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follower, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        val userId = App.sharedPreferences.getString("USER_ID", "")


        setupInitialSetting()
        viewModel.getUserFollowing(userId!!.toLong(), userCategory)

        initStateObserve()
        setupOnClickListener()

    }

    private fun setupInitialSetting() {
        if(checkUserType())
            userCategory = "Manager"
    }

    private fun checkUserType(): Boolean {
        val userType = App.sharedPreferences.getString("X_MODE", "")
        return userType == "ADMIN"
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
        setupRecyclerView(binding.rvSearchFollowing, followingRVAdapter, LinearLayoutManager.VERTICAL)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }

    fun setupOnClickListener() {

//        binding.tvSearchMenuCrag.setOnClickListener {
//            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_backgroundfill_nostroke_999radius)
//            binding.tvSearchMenuClimber.setBackgroundColor(Color.TRANSPARENT)
//            binding.rvFollowSearchCrags.visibility = View.VISIBLE
//            binding.rvSearchFollowing.visibility = View.INVISIBLE
//        }
//
//        binding.tvSearchMenuClimber.setOnClickListener {
//            binding.tvSearchMenuClimber.setBackgroundResource(R.drawable.rect_backgroundfill_nostroke_999radius)
//            binding.tvSearchMenuCrag.setBackgroundColor(Color.TRANSPARENT)
//            binding.rvSearchFollowing.visibility = View.INVISIBLE
//
//        }
    }

}