package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.createroute

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentMypageAdminCreateRouteBinding
import com.climus.climeet.presentation.base.BaseFragment
import kotlin.math.sqrt

class MyPageAdminCreateRouteFragment :
    BaseFragment<FragmentMypageAdminCreateRouteBinding>(R.layout.fragment_mypage_admin_create_route) {

    private val viewModel: MyPageAdminCreateRouteViewModel by viewModels()

    internal enum class TouchMode {
        NONE,
        ONE_FINGER,
        TWO_FINGER
    }

    private var touchMode: TouchMode? = null
    private var matrix: Matrix? = null
    private var savedMatrix: Matrix? = null
    private var startPoint: PointF? = null
    private var midPoint: PointF? = null
    private var oldDistance = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matrix = Matrix()
        savedMatrix = Matrix()
        binding.ivRoute.setOnTouchListener(onTouch)
        binding.ivRoute.scaleType = ImageView.ScaleType.MATRIX

    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouch = View.OnTouchListener { v, event ->
        if (v == binding.ivRoute) {
            val action = event.action
            when (action and MotionEvent.ACTION_MASK) {

                MotionEvent.ACTION_DOWN -> {
                    touchMode = TouchMode.ONE_FINGER
                    downSingleEvent(event)
                }

                MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount == 2) {
                    touchMode = TouchMode.TWO_FINGER
                    downMultiEvent(event)
                }

                MotionEvent.ACTION_MOVE -> if (touchMode === TouchMode.ONE_FINGER) {
                    moveSingleEvent(event)
                } else if (touchMode === TouchMode.TWO_FINGER) {
                    moveMultiEvent(event)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> touchMode = TouchMode.NONE
            }
        }
        true
    }

    private fun downSingleEvent(event: MotionEvent) {
        savedMatrix?.set(matrix)
        startPoint = PointF(event.x, event.y)
    }

    private fun downMultiEvent(event: MotionEvent) {
        oldDistance = getDistance(event)
        if (oldDistance > 5f) {
            savedMatrix?.set(matrix)
            midPoint = getMidPoint(event)
        }
    }

    private fun moveSingleEvent(event: MotionEvent) {
        matrix?.set(savedMatrix)
        matrix?.set(savedMatrix)
        matrix?.postTranslate(event.x - startPoint!!.x, event.y - startPoint!!.y)
        binding.ivRoute.imageMatrix = matrix
    }

    private fun moveMultiEvent(event: MotionEvent) {
        val newDistance = getDistance(event)
        if (newDistance > 5f) {
            matrix?.set(savedMatrix)
            val scale = newDistance / oldDistance
            matrix?.postScale(scale, scale, midPoint!!.x, midPoint!!.y)
            matrix?.postRotate(0.0.toFloat(), midPoint!!.x, midPoint!!.y)
            binding.ivRoute.imageMatrix = matrix
        }
    }

    private fun getMidPoint(e: MotionEvent): PointF {
        val x = (e.getX(0) + e.getX(1)) / 2
        val y = (e.getY(0) + e.getY(1)) / 2
        return PointF(x, y)
    }

    private fun getDistance(e: MotionEvent): Float {
        val x = e.getX(0) - e.getX(1)
        val y = e.getY(0) - e.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }



}