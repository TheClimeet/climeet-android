package com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentClimberProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.customview.stickchart.StickChartAdapter
import com.climus.climeet.presentation.ui.main.global.climerprofile.ClimberProfileViewModel
import com.climus.climeet.presentation.ui.main.global.climerprofile.adapter.HomeGymAdapter
import com.climus.climeet.presentation.ui.main.record.stats.SelectGymAdapter
import com.climus.climeet.presentation.ui.toGymProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ClimberProfileInfoFragment @Inject constructor(private val userId: Long) :
    BaseFragment<FragmentClimberProfileInfoBinding>(R.layout.fragment_climber_profile_info) {


    private val viewModel: ClimberProfileInfoViewModel by viewModels()
    private val parentViewModel: ClimberProfileViewModel by activityViewModels()
    private var adapter = SelectGymAdapter(0) {
    }

    private var homeGymPublic = false
    private var averageCompletionRatePublic = false
    private var averageCompletionLevelPublic = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.rvHomeHomegym.adapter = HomeGymAdapter()
        binding.vm = viewModel
        viewModel.setUserId(userId)

        initEventObserver()
        initStateObserve()
        initParentStateObserve()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ClimberProfileEvent.NavigateToGymProfile -> findNavController().toGymProfile(
                        it.id
                    )

                    ClimberProfileEvent.ShowPopupWindow -> showPopupWindow()
                    is ClimberProfileEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                binding.viewStickchart.setupChartData(it.chartUiList)
                adapter.submitList(it.gymList)
            }
        }
    }

    private fun initParentStateObserve() {
        repeatOnStarted {
            parentViewModel.climberPrivacySetting.collect {
                homeGymPublic = it.homeGymPublic
                averageCompletionRatePublic = it.averageCompletionRatePublic
                averageCompletionLevelPublic = it.averageCompletionLevelPublic

                if(it.averageCompletionLevelPublic || it.averageCompletionRatePublic) {
                    viewModel.getStatistics()
                }

                if(it.homeGymPublic) {
                    binding.rvHomeHomegym.visibility = View.VISIBLE
                    binding.layoutPrivacyHome.visibility = View.GONE
                    viewModel.getUserHomeGyms()
                } else {
                    binding.rvHomeHomegym.visibility = View.INVISIBLE
                    binding.layoutPrivacyHome.visibility = View.VISIBLE
                }

                if(it.averageCompletionRatePublic) {
                    binding.layoutPrivacyRate.visibility = View.INVISIBLE
                    binding.layoutAvgComplete.visibility = View.VISIBLE
                } else {
                    binding.layoutPrivacyRate.visibility = View.VISIBLE
                    binding.layoutAvgComplete.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showPopupWindow() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_select_gym, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_select_gym)
        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SelectGymAdapter(viewModel.selectedGymId.value) {
            popupWindow.dismiss()
        }
        adapter.submitList(viewModel.uiState.value.gymList)

        recyclerView.adapter = adapter

        popupWindow.elevation = 10f

        // 외부 터치 시 팝업 닫기 설정
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        popupWindow.showAsDropDown(binding.layoutToggle, 0, 10)
    }
}