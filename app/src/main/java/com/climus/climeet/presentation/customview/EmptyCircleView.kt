package com.climus.climeet.presentation.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.climus.climeet.R
import java.lang.Float.min

class EmptyCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var ecColor: Int = Color.WHITE
    private var ecText: String = ""
    private var ecTextSize: Float = 0F

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyCircleView)
        ecColor = typedArray.getColor(R.styleable.EmptyCircleView_ecColor, Color.WHITE)
        ecText = typedArray.getString(R.styleable.EmptyCircleView_ecText) ?: ""
        ecTextSize = typedArray.getDimension(R.styleable.EmptyCircleView_ecTextSize, 0F)
        typedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint().apply {
            color = ecColor
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - paint.strokeWidth

        canvas.drawCircle(centerX, centerY, radius, paint)

        val textPaint = Paint().apply {
            color = ecColor
            textSize = ecTextSize
        }

        val textWidth = textPaint.measureText(ecText)
        val bounds = Rect()
        textPaint.getTextBounds(ecText, 0, ecText.length, bounds)
        val textHeight = bounds.height()

        val yOffSet = textHeight / 2
        val xOffSet = textWidth / 2

        canvas.drawText(ecText, centerX - xOffSet, centerY + yOffSet, textPaint)
    }
}