package com.climus.climeet.presentation.ui.main.home.popularcrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.climus.climeet.MainNavDirections
import com.climus.climeet.R
import com.climus.climeet.app.App
import com.climus.climeet.databinding.FragmentPopularCragsBinding
import com.climus.climeet.databinding.FragmentPopularShortsBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.home.bestclimer.viewpager.BestClimerVPAdpater
import com.climus.climeet.presentation.ui.main.home.popularcrags.viewpager.PopularCragsVPAdpater
import com.climus.climeet.presentation.ui.toGymProfile
import com.climus.climeet.presentation.util.Constants
import com.google.android.material.tabs.TabLayoutMediator

class PopularCragsFragment : BaseFragment<FragmentPopularCragsBinding>(R.layout.fragment_popular_crags) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPopularCrags()
        setupOnClickListener()

    }

    private fun setupOnClickListener() {
        binding.icPopularCragsBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.icPopularCragsSearch.setOnClickListener {
            navigateToSearchCrag()
        }

    }



    private fun navigateToSearchCrag() {
        val action = MainNavDirections.actionHomeFragmentToSearchCragFragment()
        findNavController().navigate(action)
    }

    private fun setupPopularCrags() {
        val popularCragsAdapter = PopularCragsVPAdpater(this)
        binding.vpPopularCragsDetail.adapter = popularCragsAdapter

        val tabMenu = arrayListOf(" 팔로워순 ", " 기록순 ")
        TabLayoutMediator(binding.tbPopularCragsDetail, binding.vpPopularCragsDetail) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    // 선택한 암장 프로필로 이동
    private fun navToGymProfile(gymId: Long) {

        App.sharedPreferences.edit().putLong("gymId", gymId)
        //Log.d("gym_profile", "홈에서 암장 아이디 : $gymId)

        val access = App.sharedPreferences.getString(Constants.X_MODE, null)

        //todo 여기는 일단 구분 안해줘도 될듯!
        // - 암장관리자일때 선택한 프로필이 내 프로필일 경우만 암장관리자의 암장 프로필 접근 해야함

        findNavController().toGymProfile(gymId)
//        if (access == "CLIMER") {
//            findNavController().toGymProfile(gymId)
//        } else if (access == "ADMIN") {
//            Toast.makeText(requireContext(), "암장 관리자의 암장 프로필 접근", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "유효하지 않은 접근입니다.", Toast.LENGTH_SHORT).show()
//        }
    }
}