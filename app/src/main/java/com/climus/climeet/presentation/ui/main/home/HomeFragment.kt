package com.climus.climeet.presentation.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentHomeBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.presentation.ui.main.home.viewpager.BannerFragment
import com.climus.climeet.presentation.ui.main.home.viewpager.BannerVPAdapter

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))
        binding.homeIntroduceBannerVp.adapter = bannerAdapter
        binding.homeIntroduceBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}