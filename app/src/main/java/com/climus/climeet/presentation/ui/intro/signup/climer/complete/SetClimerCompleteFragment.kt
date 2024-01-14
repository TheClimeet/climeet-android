package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetClimerCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.signup.admin.complete.SetAdminCompleteEvent
import com.climus.climeet.presentation.ui.intro.signup.admin.complete.SetAdminCompleteFragmentDirections
import com.climus.climeet.presentation.ui.intro.signup.admin.complete.SetAdminCompleteViewModel

class SetClimerCompleteFragment : BaseFragment<FragmentSetClimerCompleteBinding>(R.layout.fragment_set_climer_complete) {

    private val viewModel: SetClimerCompleteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.ivCheck.setImageResource(R.drawable.ic_check_anim)
        val drawable = binding.ivCheck.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }

        initEventObserve()
    }

    private fun initEventObserve() {

    }

}