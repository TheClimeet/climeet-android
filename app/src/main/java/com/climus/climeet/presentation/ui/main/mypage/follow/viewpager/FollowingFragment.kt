package com.climus.climeet.presentation.ui.main.mypage.follow.viewpager

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompleteClimbingBinding
import com.climus.climeet.databinding.FragmentFollowingBinding
import com.climus.climeet.presentation.ui.main.home.viewpager.ranking.CompleteClimbingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private lateinit var binding : FragmentFollowingBinding
    private val viewModel: CompleteClimbingViewModel by viewModels()

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // api
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListener()

    }

    fun setupOnClickListener() {

        binding.tvSearchMenuCrag.setOnClickListener {
            binding.tvSearchMenuCrag.setBackgroundResource(R.drawable.rect_backgroundfill_nostroke_999radius)
            binding.tvSearchMenuClimber.setBackgroundColor(Color.TRANSPARENT);
        }

        binding.tvSearchMenuClimber.setOnClickListener {
            binding.tvSearchMenuClimber.setBackgroundResource(R.drawable.rect_backgroundfill_nostroke_999radius)
            binding.tvSearchMenuCrag.setBackgroundColor(Color.TRANSPARENT);
        }
    }

}