package com.climus.climeet.presentation.ui.intro.signup.admin.searchname

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchCragNameBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCragNameFragment :
    BaseFragment<FragmentSearchCragNameBinding>(R.layout.fragment_search_crag_name) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: SearchCragNameViewModel by viewModels()
    private var adapter: SearchCragNameAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.adminSignUpProgress(1)
        binding.vm = viewModel
        setRecycler()
        initStateObserve()
        initEventObserve()
    }

    private fun setRecycler(){
        adapter = SearchCragNameAdapter()
        binding.rvSearch.adapter = adapter
    }

    private fun initStateObserve() {
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
                    is SearchCragNameEvent.NavigateToSetCragName -> findNavController().toSetCragName(it.id, it.name, it.imgUrl)
                    is SearchCragNameEvent.NavigateToBack -> findNavController().navigateUp()
                    is SearchCragNameEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toSetCragName(id: Long, name: String , imgUrl: String){
        val action = SearchCragNameFragmentDirections.actionSearchCragNameFragmentToSetCragNameFragment(id, name, imgUrl)
        navigate(action)
    }
}