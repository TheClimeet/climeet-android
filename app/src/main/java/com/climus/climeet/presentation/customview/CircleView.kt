package com.climus.climeet.presentation.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.climus.climeet.R
import java.lang.Float.min

class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var cvColor: Int = Color.WHITE

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        cvColor = typedArray.getColor(R.styleable.CircleView_cvColor, Color.WHITE)
        typedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = cvColor

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY)

        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}