package com.climus.climeet.presentation.ui.main.shorts.bottomsheet.searchcrag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchCragBottomSheetBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsSearchCragAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCragBottomSheetFragment : BaseFragment<FragmentSearchCragBottomSheetBinding>(R.layout.fragment_search_crag_bottom_sheet) {

    private val viewModel: SearchCragBottomSheetViewModel by viewModels()
    private var adapter: ShortsSearchCragAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setRecycler()
        initStateObserve()
        initEventObserve()
    }

    private fun setRecycler(){
        adapter = ShortsSearchCragAdapter()
        binding.rvSearch.adapter = adapter
    }

    private fun initStateObserve(){
        repeatOnStarted {
            viewModel.uiState.collect {
                adapter?.setList(it.searchList, viewModel.keyword.value)
            }
        }
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is SearchCragBottomSheetEvent.NavigateToSelectSectorBottomSheet -> findNavController().toSelectSectorBottomSheet(it.id, it.name)
                    is SearchCragBottomSheetEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toSelectSectorBottomSheet(cragId: Long, cragName: String){
        val action = SearchCragBottomSheetFragmentDirections.actionSearchCragBottomSheetFragmentToSelectSectorBottomSheetFragment(cragId, cragName)
        navigate(action)
    }

}