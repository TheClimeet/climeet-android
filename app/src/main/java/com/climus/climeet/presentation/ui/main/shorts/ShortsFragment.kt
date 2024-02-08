package com.climus.climeet.presentation.ui.main.shorts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.selectsector.BottomSheetState
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.adapter.UpdatedFollowAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsFragment : BaseFragment<FragmentShortsBinding>(R.layout.fragment_shorts) {

    private val sharedViewModel: ShortsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = sharedViewModel

        binding.rvShortsThumbnail.adapter = ShortsThumbnailAdapter()
        binding.rvUpdatedFollow.adapter = UpdatedFollowAdapter()

        sharedViewModel.getUpdatedFollow()
        sharedViewModel.getShorts(ShortsOption.NEW_SORT)
        addOnScrollListener()
        initEventObserve()
    }

    private fun addOnScrollListener() {

        binding.layoutScrollview.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == binding.layoutScrollview.getChildAt(0).measuredHeight - v.measuredHeight) {
                sharedViewModel.getShorts(ShortsOption.NEXT_PAGE)
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            sharedViewModel.event.collect {
                when (it) {
                    is ShortsEvent.ShowToastMessage -> showToastMessage(it.msg)
                    is ShortsEvent.NavigateToShortsDetail -> findNavController().toShortsPlayer()
                    is ShortsEvent.NavigateToSearchCragBottomSheet -> {
                        BottomSheetState.state = "SHORTS"
                        findNavController().toShortsBottomSheet()
                    }
                }
            }
        }
    }

    private fun NavController.toShortsBottomSheet(){
        val action = ShortsFragmentDirections.actionShortsFragmentToShortsBottomSheetFragment()
        navigate(action)
    }

    private fun NavController.toShortsPlayer(){
        val action = ShortsFragmentDirections.actionShortsFragmentToShortsPlayerFragment()
        navigate(action)
    }
}