package com.climus.climeet.presentation.ui.main.home.popularcrags.viewpager

import android.os.Bundle
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
import com.climus.climeet.databinding.FragmentFollowerOrderBinding
import com.climus.climeet.databinding.FragmentRecordOrderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordOrderFragment : Fragment() {

    private lateinit var binding : FragmentRecordOrderBinding
    private val viewModel: RecordOrderViewModel by viewModels()

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

    private lateinit var records1 : TextView
    private lateinit var records2 : TextView
    private lateinit var records3 : TextView
    private lateinit var records4 : TextView
    private lateinit var records5 : TextView
    private lateinit var records6 : TextView
    private lateinit var records7 : TextView
    private lateinit var records8 : TextView
    private lateinit var records9 : TextView
    lateinit var recordsList: List<TextView>

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getGymRankingOrderFollowCount()
        initStateObserve()

    }

    private fun initView() {
        profileImg1 = binding.ivRecordRanking1
        profileImg2 = binding.ivRecordRanking2
        profileImg3 = binding.ivRecordRanking3
        profileImg4 = binding.ivRecordRanking4
        profileImg5 = binding.ivRecordRanking5
        profileImg6 = binding.ivRecordRanking6
        profileImg7 = binding.ivRecordRanking7
        profileImg8 = binding.ivRecordRanking8
        profileImg9 = binding.ivRecordRanking9
        profileImgList = listOf(
            profileImg1, profileImg2, profileImg3, profileImg4, profileImg5, profileImg6, profileImg7, profileImg8, profileImg9
        )

        gymName1 = binding.tvRecordRanking1GymName
        gymName2 = binding.tvRecordRanking2GymName
        gymName3 = binding.tvRecordRanking3GymName
        gymName4 = binding.tvRecordRanking4GymName
        gymName5 = binding.tvRecordRanking5GymName
        gymName6 = binding.tvRecordRanking6GymName
        gymName7 = binding.tvRecordRanking7GymName
        gymName8 = binding.tvRecordRanking8GymName
        gymName9 = binding.tvRecordRanking9GymName
        gymNameList = listOf(
            gymName1, gymName2, gymName3, gymName4, gymName5, gymName6, gymName7, gymName8, gymName9
        )

        records1 = binding.tvRecordRanking1Records
        records2 = binding.tvRecordRanking2Records
        records3 = binding.tvRecordRanking3Records
        records4 = binding.tvRecordRanking4Records
        records5 = binding.tvRecordRanking5Records
        records6 = binding.tvRecordRanking6Records
        records7 = binding.tvRecordRanking7Records
        records8 = binding.tvRecordRanking8Records
        records9 = binding.tvRecordRanking9Records
        recordsList = listOf(
            records1, records2, records3, records4, records5, records6, records7, records8, records9
        )

        rank1 = binding.tvRecordRankingNumber1
        rank2 = binding.tvRecordRankingNumber2
        rank3 = binding.tvRecordRankingNumber3
        rankList = listOf(rank1, rank2, rank3)
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel?.let { vm ->
                vm.uiState.collect { uiState ->
                    uiState.cragList?.let { cragList ->
                        val iterationCount = minOf(cragList.size, 9)

                        cragList.take(iterationCount).forEachIndexed { i, bestRecordGymResponse ->
                            if(bestRecordGymResponse.profileImageUrl != null) {
                                Glide.with(binding.root)
                                    .load(bestRecordGymResponse.profileImageUrl)
                                    .into(profileImgList[i])
                            }
                            if(i < 3) {
                                rankList[i].text = bestRecordGymResponse.ranking.toString()
                                rankList[i].visibility = View.VISIBLE
                            }
                            gymNameList[i].text = bestRecordGymResponse.gymName
                            recordsList[i].text = "팔로워 " + bestRecordGymResponse.thisWeekSelectionCount.toString()
                        }
                    }
                }
            }
        }
    }
}