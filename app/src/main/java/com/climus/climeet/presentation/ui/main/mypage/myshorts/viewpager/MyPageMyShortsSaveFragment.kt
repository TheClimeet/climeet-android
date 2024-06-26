package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageMyshortsSaveBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMyShortsSaveFragment() :
    BaseFragment<FragmentMypageMyshortsSaveBinding>(R.layout.fragment_mypage_myshorts_save) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()

    private var bottomScrollState = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.rvShorts.adapter = ShortsThumbnailAdapter()

        addOnScrollListener()
        initShortsEventObserve()
        initStateObserve()
    }

    override fun onResume() {
        super.onResume()

        sharedViewModel.initViewModel()
        sharedViewModel.getBookmarkedShorts(ShortsOption.NEW_SORT)
    }

    private fun addOnScrollListener() {

        binding.layoutScroll.setOnScrollChangeListener { v, _, scrollY, _, _ ->

            if (scrollY > binding.layoutScroll.getChildAt(0).measuredHeight - v.measuredHeight) {

                if (bottomScrollState) {
                    bottomScrollState = false
                    sharedViewModel.getBookmarkedShorts(ShortsOption.NEXT_PAGE)
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

}