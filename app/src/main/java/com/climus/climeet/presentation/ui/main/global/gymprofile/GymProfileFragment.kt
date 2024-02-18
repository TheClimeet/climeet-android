package com.climus.climeet.presentation.ui.main.global.gymprofile

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentGymProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GymProfileFragment : BaseFragment<FragmentGymProfileBinding>(R.layout.fragment_gym_profile) {

    private val viewModel: GymProfileViewModel by activityViewModels()
    private var adapter : GymTabAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initViewPager()
        initClickListener()
    }

    private fun initViewPager() {
        binding.vpTabDetail.adapter = adapter

        val tabMenu = arrayListOf("커뮤니티", "루트", "정보")
        TabLayoutMediator(binding.tbMenu, binding.vpTabDetail) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun initClickListener() {
        // 탭 indicator 색 바꾸기
        binding.tbMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_main),
                        BlendMode.SRC_ATOP
                    )
                    tab?.icon?.colorFilter = colorFilter
                } else {
                    tab?.icon?.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_main),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val colorFilter = BlendModeColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_grey5),
                        BlendMode.SRC_ATOP
                    )
                    tab?.icon?.colorFilter = colorFilter
                } else {
                    tab?.icon?.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.cm_grey5),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}