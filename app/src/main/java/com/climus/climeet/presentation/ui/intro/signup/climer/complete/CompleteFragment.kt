package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCompleteBinding
import com.climus.climeet.presentation.base.BaseFragment

class CompleteFragment : BaseFragment<FragmentCompleteBinding>(R.layout.fragment_complete) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkIv = binding.ivCompleteCheck

        checkIv.scaleX = 0f
        checkIv.scaleY = 1f

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                checkIv.scaleX = value
            }
        }

        animator.start()

    }

}