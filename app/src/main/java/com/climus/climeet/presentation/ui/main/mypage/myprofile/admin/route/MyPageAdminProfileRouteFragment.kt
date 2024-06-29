package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.route

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminProfileRouteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheet
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.route.GymProfileRouteEvent
import com.climus.climeet.presentation.ui.main.global.gymprofile.route.GymProfileRouteViewModel
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.RouteImageAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.MyPageAdminMyProfileViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model.MyPageAdminRouteData
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsThumbnailAdapter
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsOption
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerEvent
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsPlayerViewModel
import com.climus.climeet.presentation.ui.toShortsPlayer
import java.time.LocalDate

class MyPageAdminProfileRouteFragment: BaseFragment<FragmentMypageAdminProfileRouteBinding>(R.layout.fragment_mypage_admin_profile_route) {

    private val gymViewModel: GymProfileViewModel by activityViewModels()
    private val shortsViewModel: ShortsPlayerViewModel by activityViewModels()
    private val dateViewModel: SelectDateBottomSheetViewModel by activityViewModels()
    private val routeViewModel: GymProfileRouteViewModel by activityViewModels()
    private val parentViewModel: MyPageAdminMyProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = shortsViewModel
        binding.rvm = routeViewModel
        binding.vm = parentViewModel

        setRouteTab()
        setRecyclerView()
        initRouteEventObserve()
        initShortsEventObserve()
        addOnScrollListener()

        routeViewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            routeViewModel.setDate()
        })
    }

    private fun setRouteTab() {
        gymViewModel.gymId.observe(viewLifecycleOwner, Observer { id ->

            shortsViewModel.setCurFilter(id)
            shortsViewModel.getShorts(ShortsOption.NEW_SORT)

            routeViewModel.setCragInfo(id, gymViewModel.uiState.value.gymName)
        })
    }

    private fun addOnScrollListener() {

        binding.layoutScrollview.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == binding.layoutScrollview.getChildAt(0).measuredHeight - v.measuredHeight) {
                shortsViewModel.getShorts(ShortsOption.NEXT_PAGE)
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

    private fun initRouteEventObserve() {
        repeatOnStarted {
            routeViewModel.event.collect { event ->
                when (event) {
                    is GymProfileRouteEvent.ShowDatePicker -> {
                        SelectDateBottomSheet(
                            requireContext(),
                            dateViewModel,
                            MyPageAdminRouteData.selectedDate,
                            MyPageAdminRouteData::setSelectedDate
                        ) { date ->
                            routeViewModel.setSelectedDate(date)
                        }.show()
                    }

                    is GymProfileRouteEvent.deleteFilter -> {
                        shortsViewModel.gymProfileDelete.value = true
                        shortsViewModel.deleteFilter()
                    }

                    is GymProfileRouteEvent.ApplyFilter -> {
                        shortsViewModel.applyFilter(event.filter)
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
            shortsViewModel.event.collect {
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
}
