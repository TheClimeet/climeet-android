package com.climus.climeet.presentation.ui.main.mypage.myshorts.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.data.model.response.UserShortsVisibilityType
import com.climus.climeet.databinding.FragmentMypageMyshortsSaveBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMyShortsSaveFragment :
    BaseFragment<FragmentMypageMyshortsSaveBinding>(R.layout.fragment_mypage_myshorts_save) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private val viewModel: MyPageShortsCommentViewModel by viewModels()

    private var bottomScrollState = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel

        sharedViewModel.initViewModel()
        // todo : api 나오면 그거로 연결
        sharedViewModel.getMyShorts(ShortsOption.NEW_SORT, UserShortsVisibilityType.PUBLIC)

        addOnScrollListener()
        initShortsEventObserve()
        initStateObserve()
    }

    private fun addOnScrollListener() {

        binding.layoutScroll.setOnScrollChangeListener { v, _, scrollY, _, _ ->

            if (scrollY > binding.layoutScroll.getChildAt(0).measuredHeight - v.measuredHeight) {

                if (bottomScrollState) {
                    bottomScrollState = false
                    // todo : api 나오면 그거로 연결
                    sharedViewModel.getMyShorts(ShortsOption.NEXT_PAGE, UserShortsVisibilityType.PUBLIC)
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