package com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager

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
import com.climus.climeet.databinding.FragmentCompleteDetailBinding
import com.climus.climeet.databinding.FragmentTimeBinding
import com.climus.climeet.databinding.FragmentTimeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimeDetailFragment : Fragment() {

    private lateinit var binding : FragmentTimeDetailBinding
    private val viewModel: TimeDetailViewModel by viewModels()

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

    private lateinit var nickname1 : TextView
    private lateinit var nickname2 : TextView
    private lateinit var nickname3 : TextView
    private lateinit var nickname4 : TextView
    private lateinit var nickname5 : TextView
    private lateinit var nickname6 : TextView
    private lateinit var nickname7 : TextView
    private lateinit var nickname8 : TextView
    private lateinit var nickname9 : TextView
    lateinit var nicknameList: List<TextView>

    private lateinit var recordTime1 : TextView
    private lateinit var recordTime2 : TextView
    private lateinit var recordTime3 : TextView
    private lateinit var recordTime4 : TextView
    private lateinit var recordTime5 : TextView
    private lateinit var recordTime6 : TextView
    private lateinit var recordTime7 : TextView
    private lateinit var recordTime8 : TextView
    private lateinit var recordTime9 : TextView
    lateinit var recordList: List<TextView>

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getClimberRankingOrderTime()
        initStateObserve()

    }

    private fun initView() {
        profileImg1 = binding.ivTimeRanking1
        profileImg2 = binding.ivTimeRanking2
        profileImg3 = binding.ivTimeRanking3
        profileImg4 = binding.ivTimeRanking4
        profileImg5 = binding.ivTimeRanking5
        profileImg6 = binding.ivTimeRanking6
        profileImg7 = binding.ivTimeRanking7
        profileImg8 = binding.ivTimeRanking8
        profileImg9 = binding.ivTimeRanking9
        profileImgList = listOf(
            profileImg1, profileImg2, profileImg3, profileImg4, profileImg5, profileImg6, profileImg7, profileImg8, profileImg9
        )

        nickname1 = binding.tvTimeRanking1Nickname
        nickname2 = binding.tvTimeRanking2Nickname
        nickname3 = binding.tvTimeRanking3Nickname
        nickname4 = binding.tvTimeRanking4Nickname
        nickname5 = binding.tvTimeRanking5Nickname
        nickname6 = binding.tvTimeRanking6Nickname
        nickname7 = binding.tvTimeRanking7Nickname
        nickname8 = binding.tvTimeRanking8Nickname
        nickname9 = binding.tvTimeRanking9Nickname
        nicknameList = listOf(
            nickname1, nickname2, nickname3, nickname4, nickname5, nickname6, nickname7, nickname8, nickname9
        )

        recordTime1 = binding.tvTimeRanking1
        recordTime2 = binding.tvTimeRanking2
        recordTime3 = binding.tvTimeRanking3
        recordTime4 = binding.tvTimeRanking4
        recordTime5 = binding.tvTimeRanking5
        recordTime6 = binding.tvTimeRanking6
        recordTime7 = binding.tvTimeRanking7
        recordTime8 = binding.tvTimeRanking8
        recordTime9 = binding.tvTimeRanking9
        recordList = listOf(
            recordTime1, recordTime2, recordTime3, recordTime4, recordTime5, recordTime6, recordTime7, recordTime8, recordTime9
        )

        rank1 = binding.tvTimeRankingNumber1
        rank2 = binding.tvTimeRankingNumber2
        rank3 = binding.tvTimeRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.rankingList?.let { rankingList ->
                        Log.d("TimeDetail", rankingList.toString())
                        val iterationCount = minOf(rankingList.size, 9)

                        rankingList.take(iterationCount).forEachIndexed { i, bestTimeClimberResponse ->
                            if(bestTimeClimberResponse.profileImageUrl != null) {
                                Glide.with(binding.root)
                                    .load(bestTimeClimberResponse.profileImageUrl)
                                    .into(profileImgList[i])
                            }
                            if(i < 3) {
                                rankList[i].text = bestTimeClimberResponse.ranking.toString()
                                rankList[i].visibility = View.VISIBLE
                            }
                            nicknameList[i].text = bestTimeClimberResponse.profileName
                            recordList[i].text = bestTimeClimberResponse.thisWeekTotalClimbingTime.toString()

                            profileImgList[i].visibility = View.VISIBLE
                            nicknameList[i].visibility = View.VISIBLE
                            recordList[i].visibility = View.VISIBLE
                        }

                    }
                }
            }
        }
    }
}