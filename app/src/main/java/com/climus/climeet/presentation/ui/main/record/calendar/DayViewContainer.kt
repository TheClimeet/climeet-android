package com.climus.climeet.presentation.ui.main.record.calendar

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.climus.climeet.R
import com.climus.climeet.presentation.ui.main.record.calendar.createclimbingrecord.CreateClimbingRecordViewModel
import com.climus.climeet.presentation.ui.main.record.model.CreateRecordData
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View, val viewModel: CalendarViewModel, val dateViewModel: CreateClimbingRecordViewModel) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.tv_date)
    val squareView: View = view.findViewById(R.id.view_date)
    val titlesContainer: ViewGroup = view as ViewGroup
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            selectedDay?.deselect()  // 기존 선택된 날짜 deselect
            select()  // 새로 선택된 날짜 select
            selectedDay = this  // selectedDay 업데이트
            viewModel.setIsToday(day.date == LocalDate.now())
            viewModel.setRecord(day.date)
            viewModel.setSelectedDate(day.date)
        }
    }

    fun select() {
        textView.setTextColor(Color.BLACK)
        textView.setBackgroundResource(R.drawable.rect_mainfill_nostroke_5radius)
        squareView.setBackgroundResource(R.drawable.rect_grey6fill_nostroke_5radius)
        viewModel.setSelectedDate(day.date)
        dateViewModel.setSelectedDate(day.date)
        CreateRecordData.setSelectedDate(day.date)
    }

    fun deselect() {
        if (day.date == LocalDate.now()) {
            textView.setTextColor(ContextCompat.getColor(view.context, R.color.cm_main))
        } else {
            textView.setTextColor(Color.GRAY)
        }
        textView.setBackgroundResource(android.R.color.transparent)
        squareView.setBackgroundResource(R.drawable.rect_grey8fill_nostroke_5radius)
    }

    companion object {
        var selectedDay: DayViewContainer? = null
    }
}