package com.climus.climeet.presentation.customview.stickchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class StickChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BarChart(context, attrs, defStyleAttr) {

    private val density = context.resources.displayMetrics.density

    init {
        setPinchZoom(false)
        setDrawBarShadow(false)
        setDrawGridBackground(false)
        setTouchEnabled(false)
        drawDescription(Canvas())
        setDrawBorders(false)
        description.isEnabled = false
        legend.isEnabled = false
        axisLeft.isEnabled = false
        axisRight.isEnabled = false
        renderer = RoundedBarChartRenderer(this, this.animator, this.viewPortHandler)
    }

    fun setupChartData(data: List<StickChartUiData>) {
        val entries = data.mapIndexed { index, stickChartUiData ->
            val percent = if (stickChartUiData.percent == 0f) 0.01f else stickChartUiData.percent
            BarEntry(index.toFloat(), percent)
        }

        val dataSet = BarDataSet(entries, "Label").apply {
            val colors = data.map {
                it.levelHex?.toColorInt()
            }
            setColors(colors)
            setValueTextColors(colors)
            this.valueFormatter = PercentFormatter()
            valueTextSize = 11f
        }

        val barData = BarData(dataSet).apply {
            val barWidth = when (data.size) {
                5 -> 0.25f
                6 -> 0.23f
                7 -> 0.21f
                8 -> 0.2f
                9 -> 0.18f
                10 -> 0.18f
                11 -> 0.17f
                else -> 0.17f
            }
            setBarWidth(barWidth * density)
        }

        xAxis.run {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.WHITE
            textSize = 12f
            valueFormatter = XAxisFormatter(data)
            isGranularityEnabled = true
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setLabelCount(data.size, false)
        }

        this.extraBottomOffset = 20f
        this.data = barData
        this.invalidate()
    }

    inner class PercentFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            if(value<=0.01f){
                return "0%"
            }
            return String.format("%.0f%%", value * 100)
        }
    }

    inner class XAxisFormatter(data: List<StickChartUiData>) : ValueFormatter() {
        private val name = data.map {
            it.levelName
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return name.getOrNull(value.toInt()) ?: value.toString()
        }
    }

}

