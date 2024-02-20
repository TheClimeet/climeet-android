package com.climus.climeet.presentation.ui.main.home.viewpager.introduce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.databinding.FragmentBannerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BannerFragment(val bannerDetailInfo : BannerDetailInfoResponse) : Fragment() {

    lateinit var binding : FragmentBannerBinding
    private val viewModel: BannerViewModel by viewModels()

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
        Glide.with(this)
            .load(bannerDetailInfo.bannerImageUrl)
            .into(binding.bannerImageIv)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBannerListBetweenDates()

    }
}