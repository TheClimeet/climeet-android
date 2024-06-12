package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.createroute

import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import kotlin.math.max
import kotlin.math.min

class ScaleListener(
    val view : ImageView
) : SimpleOnScaleGestureListener() {

    private var scaleFactor = 1.0f

    override fun onScale(detector: ScaleGestureDetector): Boolean {

        scaleFactor *= detector.scaleFactor
        scaleFactor = max(0.5f, min(scaleFactor, 2.0f))
        view.scaleX = scaleFactor
        view.scaleY = scaleFactor
        return true
    }
}