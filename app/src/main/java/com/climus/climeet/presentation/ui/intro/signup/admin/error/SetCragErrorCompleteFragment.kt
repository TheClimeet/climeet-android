package com.climus.climeet.presentation.ui.intro.signup.admin.error

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetCragErrorCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment

class SetCragErrorCompleteFragment :
    BaseFragment<FragmentSetCragErrorCompleteBinding>(R.layout.fragment_set_crag_error_complete) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        setBtnListener()
    }

    private fun setBtnListener() {

        binding.btnMain.setOnClickListener {
            findNavController().toLogin()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().toLogin()
        }
    }

    private fun NavController.toLogin() {
        val action =
            SetCragErrorCompleteFragmentDirections.actionSetCragErrorCompleteFragmentToLoginFragment()
        navigate(action)
    }
}