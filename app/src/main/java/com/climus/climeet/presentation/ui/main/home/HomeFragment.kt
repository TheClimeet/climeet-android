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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.presentation.ui.main.home.recycler.homegym.HomeGymRVAdapter
import com.climus.climeet.presentation.ui.main.home.model.HomeGym
import com.climus.climeet.presentation.ui.main.home.model.PopularCrag
import com.climus.climeet.presentation.ui.main.home.model.PopularRoute
import com.climus.climeet.presentation.ui.main.home.model.PopularShorts
import com.climus.climeet.presentation.ui.main.home.recycler.popularcrag.PopularCragRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.popularroute.PopularRouteRVAdapter
import com.climus.climeet.presentation.ui.main.home.recycler.popularshorts.PopularShortsRVAdapter
import com.climus.climeet.presentation.ui.main.home.viewpager.best.RankingVPAdapter
import com.climus.climeet.presentation.ui.main.home.viewpager.introduce.BannerFragment
import com.climus.climeet.presentation.ui.main.home.viewpager.introduce.BannerVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Timer
import java.util.TimerTask

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIntroduceBanner()
        setupHomeGymList()
        setupBestRanking()
        setupPopularShorts()
        setupPopularCrags()
        setupPopularRoutes()
    }

    private fun setupIntroduceBanner() {
        val bannerAdapter = BannerVPAdapter(this, binding.vpHomeIntroduceBanner)
        repeat(3) {
            bannerAdapter.addFragment(BannerFragment(R.drawable.img_introduce_banner))
        }

        val viewPager = binding.vpHomeIntroduceBanner
        val itemCount = bannerAdapter.itemCount
        val indicator = binding.tvIndicatorFraction
        viewPager.adapter = bannerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

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
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    if (currentPos == 0) viewPager.currentItem = itemCount - 1
                    else if (currentPos == itemCount - 1) viewPager.currentItem = 0
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })

        autoSlide(bannerAdapter)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, orientation: Int) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), orientation, false)
    }

    private fun setupHomeGymList() {
        val homeGymList = arrayListOf(
            HomeGym(null, "더클라임 연남", 70),
            HomeGym(null, "더클라임 강남", 23),
            HomeGym(null, "더클라임 구로", 56),
            HomeGym(null, "더클라임 인천", 45),
            HomeGym(null, "더클라임 부산", 137),
            HomeGym(null, "더클라임 부평", 125)
        )

        val homeGymRVAdapter = HomeGymRVAdapter(homeGymList)
        setupRecyclerView(binding.rvHomeHomegym, homeGymRVAdapter, LinearLayoutManager.HORIZONTAL)
    }

    private fun setupBestRanking() {
        val rankingAdapter = RankingVPAdapter(this)
        binding.vpHomeBestRanking.adapter = rankingAdapter

        val tabMenu = arrayListOf(" 완등 ", " 시간 ", " 레벨 ")
        TabLayoutMediator(binding.tbHomeBestRanking, binding.vpHomeBestRanking) { tab, position ->
            tab.text = tabMenu[position]
        }.attach()
    }

    private fun setupPopularShorts() {
        val shortsList = arrayListOf(
            PopularShorts(null, "피커스 구로", "#000000", "V10", "#FFBEDF22"),
            PopularShorts(null, "더클라임 양재", "#FFFFFF", "V1", "#FFBEDF22"),
            PopularShorts(null, "피커스 사당", "#000000", "V9", "#FFBEDF22"),
            PopularShorts(null, "더클라임 마포", "#000000", "V3", "#FFBEDF22"),
            PopularShorts(null, "피커스 부평", "#FFFFFF", "V7", "#000FFF"),
            PopularShorts(null, "더클라임 부천", "#000000", "V2", "#000000")
        )

        val popularShortsRVAdapter = PopularShortsRVAdapter(shortsList)
        setupRecyclerView(binding.rvHomeShorts, popularShortsRVAdapter, LinearLayoutManager.HORIZONTAL)
    }

    private fun setupPopularCrags() {
        val cragList = arrayListOf(
            PopularCrag(null, true, "피커스 구로", 70),
            PopularCrag(null, false, "송도 비블럭", 125),
            PopularCrag(null, true, "더클라임 연남", 12),
            PopularCrag(null, false, "더클라임 마포", 24),
            PopularCrag(null, true, "더클라임 부천", 54),
            PopularCrag(null, false, "더클라임 부천", 90)
        )

        val popularCragRVAdapter = PopularCragRVAdapter(cragList)
        setupRecyclerView(binding.rvHomePopularCrags, popularCragRVAdapter, LinearLayoutManager.HORIZONTAL)
    }

    private fun setupPopularRoutes() {
        val routeList = arrayListOf(
            PopularRoute(null, "V1", "#63B75D", "더클라임 연남", "툇마루"),
        PopularRoute(null, "V3", "#555522", "볼더프렌즈", "섹터 A"),
        PopularRoute(null, "V10", "#FFFFFF", "웨이브락 서면", "Sector V"),
        PopularRoute(null, "V7", "#4C3E2F", "V10 천호점", "락랜드"),
        PopularRoute(null, "V2", "#333333", "더클라임 부펀", "툇마루"),
        PopularRoute(null, "V6", "#765665", "웨이브락 구로", "툇마루")
        )

        val popularRouteRVAdapter = PopularRouteRVAdapter(routeList)
        setupRecyclerView(binding.rvHomePopularRoutes, popularRouteRVAdapter, LinearLayoutManager.HORIZONTAL)
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