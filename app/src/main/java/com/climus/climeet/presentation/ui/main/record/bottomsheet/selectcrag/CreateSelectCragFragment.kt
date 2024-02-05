package com.climus.climeet.presentation.ui.main.record.bottomsheet.selectcrag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCreateSelectCragBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord.CreateClimbingRecordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateSelectCragFragment :
    BaseFragment<FragmentCreateSelectCragBinding>(R.layout.fragment_create_select_crag) {

    private val ClimbingRecordViewModel: CreateClimbingRecordViewModel by activityViewModels()
    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: CreateSelectCragViewModel by viewModels()
    private var adapter: CreateSelectCragRVAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.climerSignUpProgress(4)
        binding.vm = viewModel
        adapter = CreateSelectCragRVAdapter(viewModel, ClimbingRecordViewModel)
        binding.rvSearchCrag.adapter = adapter

        viewModel.exitSignal.observe(viewLifecycleOwner, Observer { shouldExit ->
            if (shouldExit) {
                findNavController().navigateUp()
            }
        })

        initStateObserve()
        initEventObserve()
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                adapter?.setList(it.searchList, viewModel.keyword.value)
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when(it){
                    is CreateSelectCragEvent.NavigateToBack -> findNavController().navigateUp()
                    is CreateSelectCragEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

}