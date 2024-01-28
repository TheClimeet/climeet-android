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
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompleteClimbingFragment: Fragment() {

    private lateinit var binding : FragmentCompleteClimbingBinding
    private val viewModel: CompleteClimbingViewModel by viewModels()

    private lateinit var profileImg1 : ImageView
    private lateinit var profileImg2 : ImageView
    private lateinit var profileImg3 : ImageView
    lateinit var profileImgList: List<ImageView>

    private lateinit var nickname1 : TextView
    private lateinit var nickname2 : TextView
    private lateinit var nickname3 : TextView
    lateinit var nicknameList: List<TextView>

    private lateinit var problems1 : TextView
    private lateinit var problems2 : TextView
    private lateinit var problems3 : TextView
    lateinit var problemsList: List<TextView>

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
        // Inflate the layout for this fragment using DataBindingUtil
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complete_climbing, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getClimberRankingOrderClearCount()
        initStateObserve()

    }

    private fun initView() {
        profileImg1 = binding.ivCompleteRanking1
        profileImg2 = binding.ivCompleteRanking2
        profileImg3 = binding.ivCompleteRanking3
        profileImgList = listOf(profileImg1, profileImg2, profileImg3)

        nickname1 = binding.tvCompleteRanking1Nickname
        nickname2 = binding.tvCompleteRanking2Nickname
        nickname3 = binding.tvCompleteRanking3Nickname
        nicknameList = listOf(nickname1, nickname2, nickname3)

        problems1 = binding.completeProblems1
        problems2 = binding.completeProblems2
        problems3 = binding.completeProblems3
        problemsList = listOf(problems1, problems2, problems3)

        rank1 = binding.ivCompleteRankingNumber1
        rank2 = binding.ivCompleteRankingNumber2
        rank3 = binding.ivCompleteRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.rankingList?.let { rankingList ->
                        Log.d("CompleteClimbing", rankingList.toString())
                        rankingList.take(3).forEachIndexed { i, bestClearClimberResponse ->
                            rankList[i].text = bestClearClimberResponse.ranking.toString()
                            nicknameList[i].text = bestClearClimberResponse.profileName
                            problemsList[i].text = bestClearClimberResponse.thisWeekClearCount.toString()
                        }
                    }
                }
            }
        }
    }

    //    private fun initObserve(
//        profileList: List<ImageView>,
//        nicknameList: List<TextView>,
//        problemsList: List<TextView>,
//        rankList: List<TextView>
//    ) {
//        viewModel.rankingList.observe(viewLifecycleOwner){
//            // rankingList 데이터가 바뀌었을때 여기가 호출됨
//
//            if(it?.isNotEmpty() == true){
//
//                // 아마 변수가 몇가지 존재할 수 있음
//                // LivdData 사용시  List의 원소 하나만 바꾸면은 Observe 호출이 안됨
//                Log.d("API", "Called")
//                it.take(3).forEachIndexed { i, bestClearClimberResponse ->
//                    Log.d("API", bestClearClimberResponse.toString())
//                    rankList[i].text = bestClearClimberResponse.ranking.toString()
//                    nicknameList[i].text = bestClearClimberResponse.profileName
//                    problemsList[i].text = bestClearClimberResponse.thisWeekClearCount.toString()
//                }
//
//
//            }
//        }
//    }
}