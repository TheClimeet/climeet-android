package com.climus.climeet.presentation.ui.main.shorts.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentShortsPlayerBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.MainViewModel
import com.climus.climeet.presentation.ui.main.shorts.adapter.ShortsDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsPlayerFragment: BaseFragment<FragmentShortsPlayerBinding>(R.layout.fragment_shorts_player) {

    private val sharedViewModel: ShortsPlayerViewModel by activityViewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var adapter : ShortsDetailAdapter? = null

    private val args: ShortsPlayerFragmentArgs by navArgs()
    private val curShortsId by lazy { args.shortsId }
    private val curPosition by lazy { args.position }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.changeStatusBarBlack()

        binding.position = curPosition
        binding.vm = sharedViewModel
        adapter = ShortsDetailAdapter(this)
        setViewPager()
    }

    private fun setViewPager(){
        binding.vpShorts.adapter = adapter
        binding.vpShorts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == sharedViewModel.uiState.value.shortsList.size - 1){
                    sharedViewModel.getShorts(ShortsOption.NEXT_PAGE)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        parentViewModel.changeStatusBarBackground()
    }
}