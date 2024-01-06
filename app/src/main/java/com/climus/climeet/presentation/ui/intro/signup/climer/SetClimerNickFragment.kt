package com.climus.climeet.presentation.ui.intro.signup.climer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerNickBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.login.admin.AdminLoginViewModel
import com.climus.climeet.presentation.ui.intro.login.climer.ClimerLoginEvent
import com.climus.climeet.presentation.ui.intro.login.climer.ClimerLoginFragmentDirections
import com.climus.climeet.presentation.ui.main.MainActivity

class SetClimerNickFragment :
    BaseFragment<FragmentSetClimerNickBinding>(R.layout.fragment_set_climer_nick) {

    private val viewModel: SetClimerNickViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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