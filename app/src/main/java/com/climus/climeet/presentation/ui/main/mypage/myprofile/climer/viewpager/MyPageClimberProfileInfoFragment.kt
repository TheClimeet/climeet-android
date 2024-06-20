package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.viewpager

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
import com.climus.climeet.databinding.FragmentEditClimberProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.climerprofile.adapter.HomeGymAdapter
import com.climus.climeet.presentation.ui.main.global.climerprofile.viewpager.ClimberProfileInfoViewModel
import com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.MyPageClimberProfileViewModel
import com.climus.climeet.presentation.ui.main.record.stats.SelectGymAdapter
import com.climus.climeet.presentation.ui.toGymProfile
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyPageClimberProfileInfoFragment @Inject constructor(
    private val userId: Long
) : BaseFragment<FragmentEditClimberProfileInfoBinding>(R.layout.fragment_edit_climber_profile_info) {

    private val sharedViewModel: ClimberProfileInfoViewModel by activityViewModels()
    private val mainViewModel: MyPageClimberProfileViewModel by activityViewModels()
    private val viewModel: MyPageClimberProfileInfoViewModel by viewModels()

    private var adapter = SelectGymAdapter(0) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        binding.rvHomeHomegym.adapter = HomeGymAdapter()

        binding.svm = sharedViewModel
        binding.mainvm = mainViewModel
        binding.vm = viewModel

        sharedViewModel.setUserId(userId)
        viewModel.setUserId(userId)

        sharedViewModel.getStatistics()
        sharedViewModel.getUserHomeGyms()

        initStateObserve()
        initEventObserver()
    }

    private fun initStateObserve() {
        repeatOnStarted {
            sharedViewModel.uiState.collect{
                binding.viewStickchart.setupChartData(it.chartUiList)
                adapter.submitList(it.gymList)
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MyPageClimberProfileEvent.ChangePrivacyState -> setPrivacyState(
                        it.target,
                        it.state
                    )

                    MyPageClimberProfileEvent.ShowPopupWindow -> showPopupWindow()
                    is MyPageClimberProfileEvent.NavigateToGymProfile -> findNavController().toGymProfile(
                        it.id
                    )
                }
            }
        }
    }

    // 공개 범위 설정
    private fun setPrivacyState(target: String, state: Boolean){
        mainViewModel.setPrivacyState(target, state)
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
        adapter = SelectGymAdapter(sharedViewModel.selectedGymId.value) {
            popupWindow.dismiss()
        }
        adapter.submitList(sharedViewModel.uiState.value.gymList)

        recyclerView.adapter = adapter

        popupWindow.elevation = 10f

        // 외부 터치 시 팝업 닫기 설정
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        popupWindow.showAsDropDown(binding.layoutToggle, 0, 10)
    }
}