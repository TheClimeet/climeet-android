package com.climus.climeet.presentation.ui.intro.signup.admin.error

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSetCragErrorBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetCragErrorFragment :
    BaseFragment<FragmentSetCragErrorBinding>(R.layout.fragment_set_crag_error) {

    private val viewModel: SetCragErrorViewModel by viewModels()
    private val args: SetCragErrorFragmentArgs by navArgs()
    private val cragId by lazy { args.cragId }
    private val cragImgUrl by lazy { args.cragImgUrl }
    private val cragName by lazy { args.cragName }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setCragInfo(cragId, cragImgUrl, cragName)
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SetCragErrorEvent.NavigateToBack -> findNavController().navigateUp()
                    is SetCragErrorEvent.NavigateToErrorComplete -> findNavController().toSetCragErrorComplete()
                }
            }
        }
    }

    private fun NavController.toSetCragErrorComplete() {
        val action = SetCragErrorFragmentDirections.actionSetCragErrorFragmentToSetCragErrorCompleteFragment()
        navigate(action)
    }

}