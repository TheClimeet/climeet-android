package com.climus.climeet.presentation.ui.main.global.gymprofile.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileInfoBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.GymProfileViewModel

class GymProfileInfoFragment :
    BaseFragment<FragmentGymProfileInfoBinding>(R.layout.fragment_gym_profile_info) {

    val parentViewModel : GymProfileViewModel by activityViewModels()
    val viewModel : GymProfileInfoViewModel by viewModels()

    private val args: GymProfileInfoFragmentArgs by navArgs()
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
                    is GymProfileInfoEvent.NavigateToGymReviewBottomSheetFragment -> findNavController().toGymReviewBottomSheetFragment()
                }
            }
        }
    }


    private fun NavController.toGymReviewBottomSheetFragment(){
        val action = GymProfileInfoFragmentDirections.actionGymProfileInfoFragmentToGymReviewBottomSheetFragment(gymId)
        navigate(action)
    }
}