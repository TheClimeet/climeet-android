package com.climus.climeet.presentation.ui.intro.signup.climer.setnick

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerNickBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetClimerNickFragment :
    BaseFragment<FragmentSetClimerNickBinding>(R.layout.fragment_set_climer_nick) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: SetClimerNickViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.climerSignUpProgress(1)
        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerNickEvent.NavigateToBack -> findNavController().navigateUp()

                    is SetClimerNickEvent.NavigateToSetProfile -> findNavController().toSetClimerProfile()

                    is SetClimerNickEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toSetClimerProfile() {
        val action =
            SetClimerNickFragmentDirections.actionSetClimerNameFragmentToSetClimerProfileFragment()
        navigate(action)
    }

}