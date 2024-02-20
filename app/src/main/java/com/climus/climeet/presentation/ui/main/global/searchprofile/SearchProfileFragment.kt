package com.climus.climeet.presentation.ui.main.global.searchprofile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.searchprofile.adapter.SearchProfileAdapter
import com.climus.climeet.presentation.ui.toClimerProfile
import com.climus.climeet.presentation.ui.toGymProfile
import com.climus.climeet.presentation.util.Constants.TAG
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchProfileFragment :
    BaseFragment<FragmentSearchProfileBinding>(R.layout.fragment_search_profile) {

    private val viewModel: SearchProfileViewModel by viewModels()
    private var adapter: SearchProfileAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        adapter = SearchProfileAdapter()
        binding.rvSearchResult.adapter = adapter
        initEventObserve()
        initStateObserve()
        tabListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SearchProfileEvent.NavigateToClimerProfile -> findNavController().toClimerProfile(
                        it.id
                    )

                    is SearchProfileEvent.NavigateToGymProfile -> findNavController().toGymProfile(
                        it.id
                    )

                    is SearchProfileEvent.ShowToastMessage -> showToastMessage(it.msg)
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                adapter?.setList(it.profileList, viewModel.keyword.value)
            }
        }
    }

    private fun tabListener() {
        binding.tlMode.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "암장" -> {
                        viewModel.changeMode(true)
                    }

                    "클라이머" -> {
                        viewModel.changeMode(false)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }


}