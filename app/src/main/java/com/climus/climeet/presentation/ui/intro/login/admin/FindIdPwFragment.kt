package com.climus.climeet.presentation.ui.intro.login.admin

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFindIdPwBinding
import com.climus.climeet.presentation.base.BaseFragment

class FindIdPwFragment: BaseFragment<FragmentFindIdPwBinding>(R.layout.fragment_find_id_pw) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            findNavController().toFindIdPwComplete()
        }
    }

    private fun NavController.toFindIdPwComplete(){
        val action = FindIdPwFragmentDirections.actionFindIdPwFragmentToFindIdPwCompleteFragment()
        navigate(action)
    }
}