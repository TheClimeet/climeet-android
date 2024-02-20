package com.climus.climeet.presentation.ui.main.global.gymprofile.route

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentGymProfileRouteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheet
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.RouteImageAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class GymProfileRouteFragment :
    BaseFragment<FragmentGymProfileRouteBinding>(R.layout.fragment_gym_profile_route) {

    private val parentViewModel: GymProfileViewModel by activityViewModels()
    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private val dateViewModel: SelectDateBottomSheetViewModel by viewModels()
    private val viewModel: GymProfileRouteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel
        binding.vm = viewModel

        setRecyclerView()
        initEventObserve()
        initShortsEventObserve()
        addOnScrollListener()
        setGymInfo()

        sharedViewModel.getShorts(ShortsOption.NEW_SORT)

        viewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            viewModel.setDate()
        })
    }

    private fun setGymInfo() {
        // 암장 id, 이름 설정
        parentViewModel.gymId.observe(viewLifecycleOwner, Observer { id ->
            viewModel.setCragInfo(id, parentViewModel.uiState.value.gymName)
        })
    }

    private fun addOnScrollListener() {

        binding.layoutScrollview.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == binding.layoutScrollview.getChildAt(0).measuredHeight - v.measuredHeight) {
                sharedViewModel.getShorts(ShortsOption.NEXT_PAGE)
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvShortsThumbnail.adapter = ShortsThumbnailAdapter()
        binding.rvSectorName.adapter = SectorNameAdapter()
        binding.rvSectorLevel.adapter = GymLevelAdapter()
        binding.rvSectorImage.adapter = RouteImageAdapter()
        binding.rvSectorName.itemAnimator = null
        binding.rvSectorLevel.itemAnimator = null
        binding.rvSectorImage.itemAnimator = null
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is GymProfileRouteEvent.ShowDatePicker -> {
                        SelectDateBottomSheet(
                            requireContext(),
                            dateViewModel,
                            CreateRecordData.selectedDate
                        ) { date ->
                            viewModel.setSelectedDate(date)
                        }.show()
                    }

                    is GymProfileRouteEvent.deleteFilter -> {
                        sharedViewModel.deleteFilter()
                    }

                    is GymProfileRouteEvent.ApplyFilter -> {
                        sharedViewModel.applyFilter(event.filter)
                    }

                    is GymProfileRouteEvent.ShowToastMessage -> {
                        showToastMessage(event.msg)
                    }
                }
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

                }
            }
        }
    }
}