package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageMyshortsLikeBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMyShortsLikeFragment(
    // todo: userId 넘겨받아야 할수도 (api 안 나와서 모르겠음)
    private val userId: Long = 75,
) : BaseFragment<FragmentMypageMyshortsLikeBinding>(R.layout.fragment_mypage_myshorts_like) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()

    private var bottomScrollState = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.rvShorts.adapter = ShortsThumbnailAdapter()

        sharedViewModel.initViewModel()
        // todo: api 나오면 수정
        sharedViewModel.getUserShorts(ShortsOption.NEW_SORT, userId)

        addOnScrollListener()
        initShortsEventObserve()
        initStateObserve()
    }

    private fun addOnScrollListener() {

        binding.layoutScroll.setOnScrollChangeListener { v, _, scrollY, _, _ ->

            if (scrollY > binding.layoutScroll.getChildAt(0).measuredHeight - v.measuredHeight) {

                if (bottomScrollState) {
                    bottomScrollState = false
                    // todo: api 나오면 수정
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