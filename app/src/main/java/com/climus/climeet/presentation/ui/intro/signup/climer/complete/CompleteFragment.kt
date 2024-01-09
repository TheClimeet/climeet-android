package com.climus.climeet.presentation.ui.intro.signup.climer.complete

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

        val translationXAnimator = ObjectAnimator.ofFloat(checkIv, View.TRANSLATION_X, -checkIv.width.toFloat(), 0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleXAnimator = ObjectAnimator.ofFloat(checkIv, View.SCALE_X, 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleYAnimator = ObjectAnimator.ofFloat(checkIv, View.SCALE_Y, 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationXAnimator, scaleXAnimator, scaleYAnimator)

        animatorSet.start()
    }

}