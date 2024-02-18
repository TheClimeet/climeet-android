package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentProfileReviewBottomSheetBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel

class GymReviewBottomSheetFragment : BaseFragment<FragmentProfileReviewBottomSheetBinding>(R.layout.fragment_profile_review_bottom_sheet) {

    private val viewModel: GymReviewBottomSheetFragmentViewModel by viewModels()

    private val args: GymReviewBottomSheetFragmentArgs by navArgs()
    private val gymId by lazy { args.gymId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setGymId(gymId)
        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is GymReviewBottomSheetEvent.NavigateToBack -> findNavController().toBack()
                }
            }
        }
    }

    private fun NavController.toBack(){
        val action = GymReviewBottomSheetFragmentDirections.actionGymReviewBottomSheetFragmentToGymProfileInfoFragment(gymId)
        navigate(action)
    }
}