package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.community

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminProfileCommunityBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.GymProfileCommunityViewModel
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.bestclimber.GymProfileBestClimberFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.completionrate.GymProfileAvgCompletionFragment
import com.climus.climeet.presentation.ui.main.global.gymprofile.community.skill.GymProfileSkillFragment
import com.google.android.material.tabs.TabLayout

class MyPageAdminProfileCommunityFragment: BaseFragment<FragmentMypageAdminProfileCommunityBinding>(R.layout.fragment_mypage_admin_profile_community) {

    private val sharedViewModel: GymProfileCommunityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svm = sharedViewModel

        replaceFragment(GymProfileSkillFragment())
        initTabMenu()
    }

    private fun initTabMenu() {
        binding.tbCommuMenu.addTab(binding.tbCommuMenu.newTab().setText("실력분포"))
        binding.tbCommuMenu.addTab(binding.tbCommuMenu.newTab().setText("BEST 클라이머"))
        binding.tbCommuMenu.addTab(binding.tbCommuMenu.newTab().setText("평균 완등률"))

        val tab = (binding.tbCommuMenu.getChildAt(0) as ViewGroup).getChildAt(1)
        val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 1.5f
        tab.layoutParams = layoutParams

        binding.tbCommuMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment: Fragment = when (tab.position) {
                    0 -> GymProfileSkillFragment()
                    1 -> GymProfileBestClimberFragment()
                    else -> GymProfileAvgCompletionFragment()
                }
                replaceFragment(fragment)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_menu, fragment)
        fragmentTransaction.commit()
    }
}
