package com.climus.climeet.presentation.customview.stickchart

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarShadowRectBuffer = RectF()

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = dataSet.barBorderWidth

        val drawBorder = dataSet.barBorderWidth > 0f

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        mBarShadowRectBuffer.top = 0f
        mBarShadowRectBuffer.bottom = 0f

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)

        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val radius = 10f // 상단 모서리의 둥근 반지름 값

        for (j in 0 until buffer.size() step 4) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            mRenderPaint.color = dataSet.getColor(j / 4)

            mBarShadowRectBuffer.left = left
            mBarShadowRectBuffer.right = right
            mBarShadowRectBuffer.top = top
            mBarShadowRectBuffer.bottom = bottom

            val path = Path()
            val radii = floatArrayOf(radius, radius,
                radius, radius,
                0f, 0f,
                0f, 0f)
            path.addRoundRect(mBarShadowRectBuffer, radii, Path.Direction.CW)

            c.drawPath(path, mRenderPaint)

            if (drawBorder) {
                c.drawPath(path, mBarBorderPaint)
            }
        }
    }
}