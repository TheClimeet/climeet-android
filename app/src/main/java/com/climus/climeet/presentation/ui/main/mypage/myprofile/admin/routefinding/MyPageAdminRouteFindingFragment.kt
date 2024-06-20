package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMyPageAdminRouteFindingBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheet
import com.climus.climeet.presentation.customview.selectdate.SelectDateBottomSheetViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter.LevelColorAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.adapter.RouteFindingLevelAdapter
import com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.routefinding.bottomsheet.SetLevelBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageAdminRouteFindingFragment :
    BaseFragment<FragmentMyPageAdminRouteFindingBinding>(R.layout.fragment_my_page_admin_route_finding) {

    private val dateViewModel: SelectDateBottomSheetViewModel by viewModels()
    private val viewModel: MyPageAdminRouteFindingViewModel by viewModels()
    private lateinit var lvAdapter: RouteFindingLevelAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        setRV()
        initEventObserve()
        initStateObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    MyPageAdminRouteFindingEvent.ShowDatePicker -> {
                        SelectDateBottomSheet(
                            requireContext(),
                            dateViewModel,
                            viewModel.selectedDate.value,
                            viewModel::noUse
                        ) { date ->
                            viewModel.setSelectedDate(date)
                        }.show()
                    }

                    MyPageAdminRouteFindingEvent.ShowSetLevel -> {
                        SetLevelBottomSheet(
                            requireContext(),
                            viewModel
                        ).show()
                    }
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                lvAdapter.submitList(state.selectedLevelColor)
            }
        }
    }

    private fun setRV() {
        val adapter = LevelColorAdapter(viewModel)
        binding.rvLevelColor.adapter = adapter

        lvAdapter = RouteFindingLevelAdapter(viewModel)
        binding.rvRouteFindingLevel.adapter = lvAdapter
    }
}