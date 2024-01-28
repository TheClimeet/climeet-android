package com.climus.climeet.presentation.ui.main.home.viewpager.introduce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.climus.climeet.databinding.FragmentBannerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BannerFragment(val imgRes : Int) : Fragment() {

    lateinit var binding : FragmentBannerBinding

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)
        // viewModel.
        initStateObserve()

    }

    private fun initStateObserve() {
        repeatOnStarted {
//            viewModel?.let { vm ->
//                vm.uiState.collect { uiState ->
//                    uiState.rankingList?.let { rankingList ->
//                        Log.d("CompleteClimbing", rankingList.toString())
//                        rankingList.take(3).forEachIndexed { i, bestClearClimberResponse ->
//                            rankList[i].text = bestClearClimberResponse.ranking.toString()
//                            nicknameList[i].text = bestClearClimberResponse.profileName
//                            problemsList[i].text = bestClearClimberResponse.thisWeekClearCount.toString()
//                        }
//                    }
//                }
//            }
        }
    }
}