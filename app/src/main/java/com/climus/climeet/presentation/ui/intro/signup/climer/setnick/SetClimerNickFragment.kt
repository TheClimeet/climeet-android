package com.climus.climeet.presentation.ui.intro.signup.climer.setnick

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerNickBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm

class SetClimerNickFragment :
    BaseFragment<FragmentSetClimerNickBinding>(R.layout.fragment_set_climer_nick) {

    private val viewModel: SetClimerNickViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("signupApiTest", "${ClimerSignupForm.token}\n${ClimerSignupForm.socialType}")

        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerNickEvent.NavigateToBack -> findNavController().navigateUp()

                    is SetClimerNickEvent.NavigateToSetProfile -> findNavController().toSetClimerProfile()

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