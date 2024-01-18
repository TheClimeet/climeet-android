package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.complete.SetAdminCompleteEvent
import com.climus.climeet.presentation.ui.intro.signup.admin.complete.SetAdminCompleteFragmentDirections
import com.climus.climeet.presentation.ui.intro.signup.admin.complete.SetAdminCompleteViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetClimerCompleteFragment : BaseFragment<FragmentSetClimerCompleteBinding>(R.layout.fragment_set_climer_complete) {

    private val viewModel: SetClimerCompleteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        binding.vm = viewModel

        viewModel.signUp(
            provider = ClimerSignupForm.socialType,
            accessToken = ClimerSignupForm.token,
            signUpRequest = ClimerSignupForm.toSignupRequest()
        )

        binding.ivCheck.setImageResource(R.drawable.ic_check_anim)
        val drawable = binding.ivCheck.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().toLogin()
        }
    }

    private fun NavController.toLogin(){
        val action = SetClimerCompleteFragmentDirections.actionSetClimerCompleteFragmentToLoginFragment()
        navigate(action)
    }

}