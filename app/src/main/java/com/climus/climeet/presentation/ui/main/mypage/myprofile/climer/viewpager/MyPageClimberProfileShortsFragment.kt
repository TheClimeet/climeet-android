package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.data.model.response.UserShortsVisibilityType
import com.climus.climeet.databinding.FragmentEditClimberShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import javax.inject.Inject

class MyPageClimberProfileShortsFragment @Inject constructor(
    private val userId: Long
) : BaseFragment<FragmentEditClimberShortsBinding>(R.layout.fragment_edit_climber_shorts) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private val viewModel: MyPageClimberProfileShortsViewModel by viewModels()
    private var bottomScrollState = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.vm = viewModel
        binding.rvShorts.adapter = ShortsThumbnailAdapter()

        sharedViewModel.initViewModel()
        sharedViewModel.getMyShorts(ShortsOption.NEW_SORT, UserShortsVisibilityType.PUBLIC)
        addOnScrollListener()
        initShortsEventObserve()
        initEventObserve()
        initStateObserve()
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

    private fun initShortsEventObserve() {
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

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is MyPageClimberProfileShortsEvent.SetShortsVisibility -> resetShortsVisibility(event.state)
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            sharedViewModel.uiState.collect {
                if (it.shortsThumbnailList.isEmpty()) {
                    binding.layoutNoItem.visibility = View.VISIBLE
                } else {
                    binding.layoutNoItem.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun resetShortsVisibility(type: UserShortsVisibilityType){
        sharedViewModel.initViewModel()
        sharedViewModel.getMyShorts(ShortsOption.NEW_SORT, type)
    }
}