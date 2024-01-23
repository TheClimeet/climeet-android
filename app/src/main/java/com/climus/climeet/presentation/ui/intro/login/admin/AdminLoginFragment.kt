package com.climus.climeet.presentation.ui.intro.login.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentAdminLoginBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.MainActivity

class AdminLoginFragment : BaseFragment<FragmentAdminLoginBinding>(R.layout.fragment_admin_login) {

    private val viewModel: AdminLoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is AdminLoginEvent.GoToMainActivity -> {
                        // todo 암장관리자모드 지정?
                        val intent = Intent(requireContext(), MainActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                    is AdminLoginEvent.NavigateToSignUp -> findNavController().toAnnounceAdminSignup()
                    is AdminLoginEvent.NavigateToFindAccount -> findNavController().toFindIdPw()
                    is AdminLoginEvent.NavigateToBack -> findNavController().navigateUp()
                    is AdminLoginEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toAnnounceAdminSignup() {
        val action = AdminLoginFragmentDirections.actionAdminLoginFragmentToAnnounceAdminSignupFragment()
        navigate(action)
    }

    private fun NavController.toFindIdPw(){
        val action = AdminLoginFragmentDirections.actionAdminLoginFragmentToFindIdPwFragment()
        navigate(action)
    }


}