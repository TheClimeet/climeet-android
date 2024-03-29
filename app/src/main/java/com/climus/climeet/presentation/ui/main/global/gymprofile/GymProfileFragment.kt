package com.climus.climeet.presentation.ui.main.global.gymprofile

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.climus.climeet.R
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.databinding.FragmentGymProfileBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.adapter.GymTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GymProfileFragment : BaseFragment<FragmentGymProfileBinding>(R.layout.fragment_gym_profile) {

    private val viewModel: GymProfileViewModel by activityViewModels()
    private var adapter : GymTabAdapter? = null

    private val args : GymProfileFragmentArgs by navArgs()
    private val gymId by lazy { args.gymId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initCragInfo()
        initViewPager()
        initClickListener()
        viewModel.getGymProfileInfo()
    }

    private fun initCragInfo() {
        sharedPreferences.edit().putLong("gymId", gymId).apply()
        viewModel.gymId.value = gymId
    }

    private fun initViewPager() {
        adapter = GymTabAdapter(this)
        binding.vpTabDetail.adapter = adapter

        val tabMenu = arrayListOf("커뮤니티", "루트", "정보")
        TabLayoutMediator(binding.tbMenu, binding.vpTabDetail) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun initClickListener() {

        binding.btnFollow.setOnClickListener {
            viewModel.followState.value = true
            Log.d("gym_profile", "버튼 눌림 true")
        }

        binding.btnFollowing.setOnClickListener{
            viewModel.followState.value = false
            Log.d("gym_profile", "버튼 눌림 false")
        }

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