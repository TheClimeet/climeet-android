package com.climus.climeet.presentation.ui.intro.signup.climer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerLevelBinding
import com.climus.climeet.databinding.FragmentSetClimerNickBinding
import com.climus.climeet.presentation.base.BaseFragment

class SetClimerLevelFragment :
    BaseFragment<FragmentSetClimerLevelBinding>(R.layout.fragment_set_climer_level) {

    private val viewModel: SetClimerLevelViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetClimerLevelEvent.NavigateToBack -> findNavController().navigateUp()

                }
            }
        }
    }

}