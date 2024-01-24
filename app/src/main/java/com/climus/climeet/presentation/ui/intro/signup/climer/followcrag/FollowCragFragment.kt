package com.climus.climeet.presentation.ui.intro.signup.climer.followcrag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentFollowCragBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.intro.IntroViewModel
import com.climus.climeet.presentation.ui.intro.signup.climer.followcrag.adapter.FollowCragRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class FollowCragFragment :
    BaseFragment<FragmentFollowCragBinding>(R.layout.fragment_follow_crag) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: FollowCragViewModel by viewModels()
    private var adapter: FollowCragRVAdapter? = null
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.climerSignUpProgress(4)
        binding.vm = viewModel
        adapter = FollowCragRVAdapter()
        binding.rvFollowCrags.adapter = adapter

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

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is FollowCragEvent.NavigateToHowToKnow -> findNavController().toHowToKnow()
                    is FollowCragEvent.NavigateToBack -> findNavController().navigateUp()
                    is FollowCragEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun NavController.toHowToKnow() {
        val action = FollowCragFragmentDirections.actionFollowCragFragmentToHowToKnowFragment3()
        navigate(action)
    }

}