package com.climus.climeet.presentation.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentHomeBinding
import com.climus.climeet.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.presentation.ui.main.home.recycler.homegym.HomeGymRVAdapter
import com.climus.climeet.presentation.ui.main.home.model.HomeGym
import com.climus.climeet.presentation.ui.main.home.viewpager.best.RankingVPAdapter
import com.climus.climeet.presentation.ui.main.home.viewpager.introduce.BannerFragment
import com.climus.climeet.presentation.ui.main.home.viewpager.introduce.BannerVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private var homeGymList = ArrayList<HomeGym>()
    private val tabMenu = arrayListOf("완등", "시간", "레벨")
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bannerAdapter = BannerVPAdapter(this, binding.vpHomeIntroduceBanner)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))

        val viewPager = binding.vpHomeIntroduceBanner
        val itemCount = bannerAdapter.itemCount
        val indicator = binding.tvIndicatorFraction
        viewPager.adapter = bannerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        indicator.text = getString(R.string.viewpager2_banner, 1, itemCount)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var currentState = 0
            var currentPos = 0

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPos = position
                indicator.text = getString(R.string.viewpager2_banner, position + 1, itemCount)
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if(currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    if(currentPos == 0) viewPager.currentItem = itemCount - 1
                    else if(currentPos == itemCount - 1) viewPager.currentItem = 0
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })

        autoSlide(bannerAdapter)

        homeGymList.apply {
            add(HomeGym(null, "더클라임 연남", 70))
            add(HomeGym(null, "더클라임 강남", 23))
            add(HomeGym(null, "더클라임 구로", 56))
            add(HomeGym(null, "더클라임 인천", 45))
            add(HomeGym(null, "더클라임 부산", 137))
            add(HomeGym(null, "더클라임 부평", 125))
        }

        val homeGymRVAdapter = HomeGymRVAdapter(homeGymList)
        binding.rvHomeHomegym.adapter = homeGymRVAdapter
        binding.rvHomeHomegym.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        val rankingAdapter = RankingVPAdapter(this)
        binding.vpHomeBestRanking.adapter = rankingAdapter

        TabLayoutMediator(binding.tbHomeBestRanking, binding.vpHomeBestRanking) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun autoSlide(adapter: BannerVPAdapter) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val nextItem = binding.vpHomeIntroduceBanner.currentItem + 1
                    if (nextItem < adapter.itemCount) {
                        binding.vpHomeIntroduceBanner.currentItem = nextItem
                    } else {
                        binding.vpHomeIntroduceBanner.currentItem = 0 // 순환
                    }
                }
            }
        }, 3000, 3000)
    }
}