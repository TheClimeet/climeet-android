package com.climus.climeet.presentation.ui.main.shorts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.selectsector.BottomSheetState
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.UpdatedFollowAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShortsFragment : BaseFragment<FragmentShortsBinding>(R.layout.fragment_shorts) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private val viewModel: ShortsViewModel by viewModels()

    private var bottomScrollState = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.vm = viewModel

        binding.rvShortsThumbnail.adapter = ShortsThumbnailAdapter()
        binding.rvUpdatedFollow.adapter = UpdatedFollowAdapter()

        sharedViewModel.getUpdatedFollow()
        sharedViewModel.getShorts(ShortsOption.NEW_SORT)
        addOnScrollListener()
        initEventObserve()
    }

    private fun addOnScrollListener() {

        binding.layoutScrollview.setOnScrollChangeListener { v, _, scrollY, _, _ ->

            if (scrollY > binding.layoutScrollview.getChildAt(0).measuredHeight - v.measuredHeight) {

                if(bottomScrollState){
                    bottomScrollState = false
                    sharedViewModel.getShorts(ShortsOption.NEXT_PAGE)
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

                }
            }
        }

        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ShortsEvent.NavigateToSearchCragBottomSheet -> {
                        BottomSheetState.state = "SHORTS"
                        findNavController().toShortsBottomSheet()
                    }
                }
            }
        }
    }

    private fun NavController.toShortsBottomSheet() {
        val action = ShortsFragmentDirections.actionShortsFragmentToShortsBottomSheetFragment()
        navigate(action)
    }

}