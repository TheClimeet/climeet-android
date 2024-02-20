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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.climus.climeet.MainNavDirections
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompleteDetailBinding
import com.climus.climeet.databinding.FragmentLevelBinding
import com.climus.climeet.databinding.FragmentLevelDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LevelDetailFragment : Fragment() {

    private lateinit var binding : FragmentLevelDetailBinding
    private val viewModel: LevelDetailViewModel by viewModels()

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

    private lateinit var level1 : TextView
    private lateinit var level2 : TextView
    private lateinit var level3 : TextView
    private lateinit var level4 : TextView
    private lateinit var level5 : TextView
    private lateinit var level6 : TextView
    private lateinit var level7 : TextView
    private lateinit var level8 : TextView
    private lateinit var level9 : TextView
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level_detail, container, false)
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
        profileImg4 = binding.ivLevelRanking4
        profileImg5 = binding.ivLevelRanking5
        profileImg6 = binding.ivLevelRanking6
        profileImg7 = binding.ivLevelRanking7
        profileImg8 = binding.ivLevelRanking8
        profileImg9 = binding.ivLevelRanking9
        profileImgList = listOf(
            profileImg1, profileImg2, profileImg3, profileImg4, profileImg5, profileImg6, profileImg7, profileImg8, profileImg9
        )

        nickname1 = binding.tvLevelRanking1Nickname
        nickname2 = binding.tvLevelRanking2Nickname
        nickname3 = binding.tvLevelRanking3Nickname
        nickname4 = binding.tvLevelRanking4Nickname
        nickname5 = binding.tvLevelRanking5Nickname
        nickname6 = binding.tvLevelRanking6Nickname
        nickname7 = binding.tvLevelRanking7Nickname
        nickname8 = binding.tvLevelRanking8Nickname
        nickname9 = binding.tvLevelRanking9Nickname
        nicknameList = listOf(
            nickname1, nickname2, nickname3, nickname4, nickname5, nickname6, nickname7, nickname8, nickname9
        )

        level1 = binding.tvLevelRanking1
        level2 = binding.tvLevelRanking2
        level3 = binding.tvLevelRanking3
        level4 = binding.tvLevelRanking4
        level5 = binding.tvLevelRanking5
        level6 = binding.tvLevelRanking6
        level7 = binding.tvLevelRanking7
        level8 = binding.tvLevelRanking8
        level9 = binding.tvLevelRanking9
        levelList = listOf(
            level1, level2, level3, level4, level5, level6, level7, level8, level9
        )

        rank1 = binding.tvLevelRankingNumber1
        rank2 = binding.tvLevelRankingNumber2
        rank3 = binding.tvLevelRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.rankingList?.let { rankingList ->
                        Log.d("LevelDetail", rankingList.toString())
                        val iterationCount = minOf(rankingList.size, 9)

                        rankingList.take(iterationCount).forEachIndexed { i, bestLevelClimberResponse ->
                            if(bestLevelClimberResponse.profileImageUrl != null) {
                                Glide.with(binding.root)
                                    .load(bestLevelClimberResponse.profileImageUrl)
                                    .into(profileImgList[i])
                                profileImgList[i].setOnClickListener {
                                    navigateToClimberProfile(bestLevelClimberResponse.userId)
                                }
                            }
                            if(i < 3) {
                                rankList[i].text = bestLevelClimberResponse.ranking.toString()
                                rankList[i].visibility = View.VISIBLE
                            }
                            nicknameList[i].text = bestLevelClimberResponse.profileName
                            levelList[i].text = "레벨 " + bestLevelClimberResponse.thisWeekHighDifficulty.toString()

                            profileImgList[i].visibility = View.VISIBLE
                            nicknameList[i].visibility = View.VISIBLE
                            levelList[i].visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun navigateToClimberProfile(userId : Long) {
        val action = MainNavDirections.globalActionToClimerProfileFragment(userId)
        findNavController().navigate(action)
    }

}