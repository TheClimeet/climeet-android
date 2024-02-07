package com.climus.climeet.presentation.ui.main.home.popularcrags.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompleteClimbingBinding
import com.climus.climeet.databinding.FragmentFollowerOrderBinding
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerOrderFragment : Fragment() {

    private lateinit var binding : FragmentFollowerOrderBinding
    private val viewModel: FollowerOrderViewModel by viewModels()

    private lateinit var profileImg1 : ImageView
    private lateinit var profileImg2 : ImageView
    private lateinit var profileImg3 : ImageView
    private lateinit var profileImg4 : ImageView
    private lateinit var profileImg5 : ImageView
    private lateinit var profileImg6 : ImageView
    private lateinit var profileImg7 : ImageView
    private lateinit var profileImg8 : ImageView
    private lateinit var profileImg9 : ImageView
    lateinit var profileImgList: List<ImageView>

    private lateinit var gymName1 : TextView
    private lateinit var gymName2 : TextView
    private lateinit var gymName3 : TextView
    private lateinit var gymName4 : TextView
    private lateinit var gymName5 : TextView
    private lateinit var gymName6 : TextView
    private lateinit var gymName7 : TextView
    private lateinit var gymName8 : TextView
    private lateinit var gymName9 : TextView
    lateinit var gymNameList: List<TextView>

    private lateinit var followers1 : TextView
    private lateinit var followers2 : TextView
    private lateinit var followers3 : TextView
    private lateinit var followers4 : TextView
    private lateinit var followers5 : TextView
    private lateinit var followers6 : TextView
    private lateinit var followers7 : TextView
    private lateinit var followers8 : TextView
    private lateinit var followers9 : TextView
    lateinit var followersList: List<TextView>

    private lateinit var rank1 : TextView
    private lateinit var rank2 : TextView
    private lateinit var rank3 : TextView
    lateinit var rankList: List<TextView>

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follower_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getGymRankingOrderFollowCount()
        initStateObserve()

    }

    private fun initView() {
        profileImg1 = binding.ivFollowersRanking1
        profileImg2 = binding.ivFollowersRanking2
        profileImg3 = binding.ivFollowersRanking3
        profileImg4 = binding.ivFollowersRanking4
        profileImg5 = binding.ivFollowersRanking5
        profileImg6 = binding.ivFollowersRanking6
        profileImg7 = binding.ivFollowersRanking7
        profileImg8 = binding.ivFollowersRanking8
        profileImg9 = binding.ivFollowersRanking9
        profileImgList = listOf(
            profileImg1, profileImg2, profileImg3, profileImg4, profileImg5, profileImg6, profileImg7, profileImg8, profileImg9
        )

        gymName1 = binding.tvFollowersRanking1GymName
        gymName2 = binding.tvFollowersRanking2GymName
        gymName3 = binding.tvFollowersRanking3GymName
        gymName4 = binding.tvFollowersRanking4GymName
        gymName5 = binding.tvFollowersRanking5GymName
        gymName6 = binding.tvFollowersRanking6GymName
        gymName7 = binding.tvFollowersRanking7GymName
        gymName8 = binding.tvFollowersRanking8GymName
        gymName9 = binding.tvFollowersRanking9GymName
        gymNameList = listOf(
            gymName1, gymName2, gymName3, gymName4, gymName5, gymName6, gymName7, gymName8, gymName9
        )

        followers1 = binding.tvFollowersRanking1Followers
        followers2 = binding.tvFollowersRanking2Followers
        followers3 = binding.tvFollowersRanking3Followers
        followers4 = binding.tvFollowersRanking4Followers
        followers5 = binding.tvFollowersRanking5Followers
        followers6 = binding.tvFollowersRanking6Followers
        followers7 = binding.tvFollowersRanking7Followers
        followers8 = binding.tvFollowersRanking8Followers
        followers9 = binding.tvFollowersRanking9Followers
        followersList = listOf(
            followers1, followers2, followers3, followers4, followers5, followers6, followers7, followers8, followers9
        )

        rank1 = binding.tvFollowersRankingNumber1
        rank2 = binding.tvFollowersRankingNumber2
        rank3 = binding.tvFollowersRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.cragList?.let { cragList ->
                        val iterationCount = minOf(cragList.size, 9)

                        cragList.take(iterationCount).forEachIndexed { i, bestFollowGymResponse ->
                            if(bestFollowGymResponse.profileImageUrl != null) {
                                Glide.with(binding.root)
                                    .load(bestFollowGymResponse.profileImageUrl)
                                    .into(profileImgList[i])
                            }
                            if(i < 3) {
                                rankList[i].text = bestFollowGymResponse.ranking.toString()
                                rankList[i].visibility = View.VISIBLE
                            }
                            gymNameList[i].text = bestFollowGymResponse.gymName
                            followersList[i].text = "팔로워 " + bestFollowGymResponse.thisWeekFollowerCount.toString()
                        }
                    }
                }
            }
        }
    }
}