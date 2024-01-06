package com.climus.climeet.presentation.ui.intro.signup.admin.complete

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetAdminCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment

class SetAdminCompleteFragment : BaseFragment<FragmentSetAdminCompleteBinding>(R.layout.fragment_set_admin_complete) {

    private val viewModel: SetAdminCompleteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetAdminCompleteEvent.NavigateToBack -> findNavController().navigateUp() // 알림 설정으로 이동
                    is SetAdminCompleteEvent.NavigateToNext -> navigateNext()
                }
            }
        }
    }

    // 로그인으로 이동
    private fun navigateNext(){
        val action = SetAdminCompleteFragmentDirections.actionSetAdminCompleteFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}