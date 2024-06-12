package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimberShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.climerprofile.ClimberProfileViewModel
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClimberProfileShortsFragment @Inject constructor(
    private val userId: Long
) :
    BaseFragment<FragmentClimberShortsBinding>(R.layout.fragment_climber_shorts) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private val parentViewModel: ClimberProfileViewModel by activityViewModels()

    private var bottomScrollState = true
    private var shortsPrivacy = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.rvShorts.adapter = ShortsThumbnailAdapter()

        sharedViewModel.initViewModel()
        addOnScrollListener()
        initEventObserve()
        initStateObserve()
        initParentStateObserve()
    }

    private fun addOnScrollListener() {

        binding.layoutScroll.setOnScrollChangeListener { v, _, scrollY, _, _ ->

            if (scrollY > binding.layoutScroll.getChildAt(0).measuredHeight - v.measuredHeight) {

                if (bottomScrollState) {
                    bottomScrollState = false
                    sharedViewModel.getUserShorts(ShortsOption.NEXT_PAGE, userId)
                }
            } else {
                bottomScrollState = true
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            sharedViewModel.event.collect {
                when (it) {
                    is ShortsPlayerEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is ShortsPlayerEvent.NavigateToShortsPlayer -> findNavController().toShortsPlayer(
                        it.shortsId,
                        it.position
                    )

                    else -> {}
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            sharedViewModel.uiState.collect {
                if(shortsPrivacy) {
                    if (it.shortsThumbnailList.isEmpty()) {
                        binding.layoutNoItem.visibility = View.VISIBLE
                    } else {
                        binding.layoutNoItem.visibility = View.INVISIBLE
                    }
                } else {
                    binding.layoutNoItem.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initParentStateObserve() {
        repeatOnStarted {
            parentViewModel.climberPrivacySetting.collect {
                shortsPrivacy = it.shortsPublic
                if(parentViewModel.climberPrivacySetting.value.shortsPublic) {
                    binding.layoutPrivacy.visibility = View.GONE
//                    binding.btnFilterToggle.visibility = View.VISIBLE
                    sharedViewModel.getUserShorts(ShortsOption.NEW_SORT, userId)
                } else {
                    binding.layoutPrivacy.visibility = View.VISIBLE
//                    binding.btnFilterToggle.visibility = View.GONE
                }
            }
        }
    }

}