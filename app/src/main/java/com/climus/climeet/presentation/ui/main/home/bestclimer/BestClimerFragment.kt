package com.climus.climeet.presentation.ui.main.home.bestclimer

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.climus.climeet.MainNavDirections
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentBestClimerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager.BestClimerVPAdpater
import com.climus.climeet.presentation.ui.toSearchProfile
import com.google.android.material.tabs.TabLayoutMediator

class BestClimerFragment : BaseFragment<FragmentBestClimerBinding>(R.layout.fragment_best_climer) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBestClimer()
        setupOnClickListener()
        binding.icBestClimerBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupOnClickListener() {
        binding.icBestClimerSearch.setOnClickListener {
            findNavController().toSearchProfile()
        }
    }

    private fun setupBestClimer() {
        val bestClimerAdapter = BestClimerVPAdpater(this)
        binding.vpBestClimerDetail.adapter = bestClimerAdapter

        val tabMenu = arrayListOf(" 완등 ", " 시간 ", " 레벨 ")
        TabLayoutMediator(binding.tbBestClimerDetail, binding.vpBestClimerDetail) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

}