package com.climus.climeet.presentation.ui.main.global.gymprofile.route

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileRouteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheet
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.global.selectsector.SelectSectorBottomSheetFragmentArgs
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.GymLevelAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.RouteImageAdapter
import com.climus.climeet.presentation.ui.main.global.selectsector.adapter.SectorNameAdapter
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData

class GymProfileRouteFragment :
    BaseFragment<FragmentGymProfileRouteBinding>(R.layout.fragment_gym_profile_route) {

    private val dateViewModel: SelectDateBottomSheetViewModel by viewModels()
    private val viewModel: GymProfileRouteViewModel by viewModels()

    private val args: SelectSectorBottomSheetFragmentArgs by navArgs()
    private val cragId by lazy { args.cragId }
    private val cragName by lazy { args.cragName }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setCragInfo(cragId, cragName)
        setRecyclerView()
        initEventObserve()
    }

    private fun setRecyclerView() {
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
                    is GymProfileRouteEvent.ApplyFilter -> {
                        viewModel.applyFilter(event.filter)
                    }
                    is GymProfileRouteEvent.DismissDialog -> {
                        viewModel.dismissDialog()
                    }
                    is GymProfileRouteEvent.ShowToastMessage -> {
                        showToastMessage(event.msg)
                    }
                }
            }
        }
    }
}