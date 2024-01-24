package com.climus.climeet.presentation.ui.intro.login

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentLoginBinding
import com.climus.climeet.presentation.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnClimerLogin.setOnClickListener {
            binding.btnAdminLogin.isEnabled = false
            binding.btnClimerLogin.setBackgroundResource(R.drawable.rect_mainfill_nostroke_8radius)
            CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                findNavController().toClimerLogin()
            }
        }

        binding.btnAdminLogin.setOnClickListener {
            binding.btnClimerLogin.isEnabled = false
            binding.btnAdminLogin.setBackgroundResource(R.drawable.rect_mainfill_nostroke_8radius)
            CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                findNavController().toAdminLogin()
            }
        }
    }

    private fun NavController.toClimerLogin() {
        val action = LoginFragmentDirections.actionLoginFragmentToClimerLoginFragment()
        navigate(action)
    }

    private fun NavController.toAdminLogin() {
        val action = LoginFragmentDirections.actionLoginFragmentToAdminLoginFragment()
        navigate(action)
    }

}