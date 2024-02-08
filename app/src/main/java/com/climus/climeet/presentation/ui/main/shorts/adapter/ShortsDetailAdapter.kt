package com.climus.climeet.presentation.ui.main.shorts.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.climus.climeet.presentation.ui.main.shorts.model.ShortsUiData
import com.climus.climeet.presentation.ui.main.shorts.player.ShortsDetailFragment

class ShortsDetailAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

   var data: List<ShortsUiData> = emptyList()

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment =
        ShortsDetailFragment(data[position])
}