package com.climus.climeet.presentation.ui.main.home.viewpager.best

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
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompleteClimbingBinding
import com.climus.climeet.databinding.FragmentTimeBinding
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingViewModel
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.TimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimeFragment : Fragment() {

    private lateinit var binding : FragmentTimeBinding
    private val viewModel: TimeViewModel by viewModels()

    private lateinit var profileImg1 : ImageView
    private lateinit var profileImg2 : ImageView
    private lateinit var profileImg3 : ImageView
    lateinit var profileImgList: List<ImageView>

    private lateinit var nickname1 : TextView
    private lateinit var nickname2 : TextView
    private lateinit var nickname3 : TextView
    lateinit var nicknameList: List<TextView>

    private lateinit var recordTime1 : TextView
    private lateinit var recordTime2 : TextView
    private lateinit var recordTime3 : TextView
    lateinit var recordTimeList: List<TextView>

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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time, container, false)
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
        profileImgList = listOf(profileImg1, profileImg2, profileImg3)

        nickname1 = binding.tvTimeRanking1Nickname
        nickname2 = binding.tvTimeRanking2Nickname
        nickname3 = binding.tvTimeRanking3Nickname
        nicknameList = listOf(nickname1, nickname2, nickname3)

        recordTime1 = binding.timeRecord1
        recordTime2 = binding.timeRecord2
        recordTime3 = binding.timeRecord3
        recordTimeList = listOf(recordTime1, recordTime2, recordTime3)

        rank1 = binding.iVTimeRankingNumber1
        rank2 = binding.ivTimeRankingNumber2
        rank3 = binding.ivTimeRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.rankingList?.let { rankingList ->
                        Log.d("TimeFragment", rankingList.toString())
                        rankingList.take(3).forEachIndexed { i, bestTimeClimberSimpleResponse ->
                            rankList[i].text = bestTimeClimberSimpleResponse.ranking.toString()
                            nicknameList[i].text = bestTimeClimberSimpleResponse.profileName
                            recordTimeList[i].text = bestTimeClimberSimpleResponse.thisWeekTotalClimbingTime.toString()
                        }
                    }
                }
            }
        }
    }
}