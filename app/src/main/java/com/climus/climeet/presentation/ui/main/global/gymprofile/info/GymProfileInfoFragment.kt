package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymPriceAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymServiceAdapter
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymTimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileInfoFragment :
    BaseFragment<FragmentGymProfileInfoBinding>(R.layout.fragment_gym_profile_info) {

    val parentViewModel: GymProfileViewModel by activityViewModels()
    val viewModel: GymProfileInfoViewModel by viewModels()

    private var timeAdapter: GymTimeAdapter? = null
    private var serviceAdapter: GymServiceAdapter? = null
    private var priceAdapter: GymPriceAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.parentVM = parentViewModel
        binding.vm = viewModel

        initStateObserve()
        initEventObserve()
        setAdapters()
        setGymInfo()
    }

    private fun setGymInfo() {
        // 암장 id, 이름 설정
        parentViewModel.gymId.observe(viewLifecycleOwner, Observer { id ->
            viewModel.setCragId(id)
            viewModel.getGymTabInfo()
        })
    }

    private fun setAdapters() {
        timeAdapter = GymTimeAdapter()
        serviceAdapter = GymServiceAdapter()
        priceAdapter = GymPriceAdapter()

        binding.rvTime.adapter = timeAdapter
        binding.rvPrice.adapter = priceAdapter
        binding.rvService.adapter = serviceAdapter
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                serviceAdapter?.setList(it.gymServiceList)
                timeAdapter?.setList(it.gymBusinessHours)
                priceAdapter?.setList(it.gymPriceList)
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is GymProfileInfoEvent.NavigateToGymReviewBottomSheetFragment -> findNavController().toGymReviewBottomSheetFragment()
                }
            }
        }
    }

    private fun NavController.toGymReviewBottomSheetFragment() {
        val action =
            GymProfileInfoFragmentDirections.actionGymProfileInfoFragmentToGymReviewBottomSheetFragment()
        navigate(action)
    }
}