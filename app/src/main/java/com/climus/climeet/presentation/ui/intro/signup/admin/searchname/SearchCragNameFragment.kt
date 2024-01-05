package com.climus.climeet.presentation.ui.intro.signup.admin.searchname

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentSearchCragNameBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCragNameFragment: BaseFragment<FragmentSearchCragNameBinding>(R.layout.fragment_search_crag_name) {

    private val viewModel: SearchCragNameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
}