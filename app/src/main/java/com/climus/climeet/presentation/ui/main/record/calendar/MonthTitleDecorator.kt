package com.climus.climeet.presentation.ui.main.record.calendar

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.style.LineBackgroundSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Calendar

class MonthTitleDecorator : DayViewDecorator {

    private val calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.month == calendar.get(Calendar.MONTH) + 1 && day.year == calendar.get(Calendar.YEAR)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(ColorDrawable(Color.TRANSPARENT))
        view.addSpan(object : LineBackgroundSpan {
            override fun drawBackground(
                canvas: Canvas, paint: Paint,
                left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
                text: CharSequence, start: Int, end: Int, lnum: Int
            ) {
                val oldColor = paint.color
                paint.color = Color.WHITE
                canvas.drawText("${calendar.get(Calendar.YEAR)}. ${calendar.get(Calendar.MONTH) + 1}", (left + right) / 2.toFloat(), bottom.toFloat(), paint)
                paint.color = oldColor
            }
        })
    }
}