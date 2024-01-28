package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.searchcrag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchCragBottomSheetBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.ShortsFilterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCragBottomSheetFragment : BaseFragment<FragmentSearchCragBottomSheetBinding>(R.layout.fragment_search_crag_bottom_sheet) {

    private val viewModel: SearchCragBottomSheetViewModel by viewModels()
    private val shortsFilterViewModel: ShortsFilterViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initObserve()
    }

    private fun initObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is SearchCragBottomSheetEvent.NavigateToSelectSectorBottomSheet -> findNavController().toSelectSectorBottomSheet()
                }
            }
        }
    }

    private fun NavController.toSelectSectorBottomSheet(){
        val action = SearchCragBottomSheetFragmentDirections.actionSearchCragBottomSheetFragmentToSelectSectorBottomSheetFragment()
        navigate(action)
    }

}