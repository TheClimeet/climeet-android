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
import com.bumptech.glide.Glide
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompleteClimbingBinding
import com.climus.climeet.databinding.FragmentLevelBinding
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingViewModel
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.LevelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LevelFragment : Fragment() {

    private lateinit var binding : FragmentLevelBinding
    private val viewModel: LevelViewModel by viewModels()

    private lateinit var profileImg1 : ImageView
    private lateinit var profileImg2 : ImageView
    private lateinit var profileImg3 : ImageView
    lateinit var profileImgList: List<ImageView>

    private lateinit var nickname1 : TextView
    private lateinit var nickname2 : TextView
    private lateinit var nickname3 : TextView
    lateinit var nicknameList: List<TextView>

    private lateinit var level1 : TextView
    private lateinit var level2 : TextView
    private lateinit var level3 : TextView
    lateinit var levelList: List<TextView>

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getClimberRankingOrderLevel()
        initStateObserve()

    }

    private fun initView() {
        profileImg1 = binding.ivLevelRanking1
        profileImg2 = binding.ivLevelRanking2
        profileImg3 = binding.ivLevelRanking3
        profileImgList = listOf(profileImg1, profileImg2, profileImg3)

        nickname1 = binding.tvLevelRanking1Nickname
        nickname2 = binding.tvLevelRanking2Nickname
        nickname3 = binding.tvLevelRanking3Nickname
        nicknameList = listOf(nickname1, nickname2, nickname3)

        level1 = binding.levelDifficulty1
        level2 = binding.levelDifficulty2
        level3 = binding.levelDifficulty3
        levelList = listOf(level1, level2, level3)

        rank1 = binding.ivLevelRankingNumber1
        rank2 = binding.ivLevelRankingNumber2
        rank3 = binding.ivLevelRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.rankingList?.let { rankingList ->
                        Log.d("LevelFragment", rankingList.toString())
                        val iterationCount = minOf(rankingList.size, 3)

                        rankingList.take(iterationCount).forEachIndexed { i, bestLevelResponse ->
                            if(bestLevelResponse.profileImageUrl != null) {
                                Glide.with(binding.root)
                                    .load(bestLevelResponse.profileImageUrl)
                                    .into(profileImgList[i])
                            }
                            rankList[i].text = bestLevelResponse.ranking.toString()
                            nicknameList[i].text = bestLevelResponse.profileName
                            levelList[i].text = "레벨 " + bestLevelResponse.thisWeekHighDifficulty.toString()
                        }
                    }
                }
            }
        }
    }
}