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
        repeatOnStarted {
            viewModel.selectedColor.collect {
                val isCompletable = !viewModel.isColorAlreadySelected()
                if (isCompletable) {
                    binding.tvExplain.text = "${viewModel.selectedColor.value.name} 레벨은 이미 등록되어 있어요"
                    binding.tvExplain.setTextColor(resources.getColor(R.color.cm_red))
                } else {
                    binding.tvExplain.text = ""
                    binding.tvExplain.setTextColor(resources.getColor(R.color.cm_main))
                }
                if(it.name == "-") {
                    val adapter = LevelColorAdapter(viewModel)
                    adapter.submitList(viewModel.colorList)
                    binding.rvLevelColor.adapter = adapter
                }
            }
        }
    }

    private fun setRV() {
//        val adapter = LevelColorAdapter(viewModel)
//        binding.rvLevelColor.adapter = adapter

        lvAdapter = RouteFindingLevelAdapter(viewModel)
        binding.rvRouteFindingLevel.adapter = lvAdapter
    }
}