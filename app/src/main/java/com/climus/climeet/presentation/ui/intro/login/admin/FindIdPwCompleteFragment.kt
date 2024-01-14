package com.climus.climeet.presentation.ui.intro.login.admin

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFindIdPwCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment

class FindIdPwCompleteFragment: BaseFragment<FragmentFindIdPwCompleteBinding>(R.layout.fragment_find_id_pw_complete) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMain.setOnClickListener {
            findNavController().toLogin()
        }
    }

    private fun NavController.toLogin(){
        val action = FindIdPwCompleteFragmentDirections.actionFindIdPwCompleteFragmentToLoginFragment()
        navigate(action)
    }
}