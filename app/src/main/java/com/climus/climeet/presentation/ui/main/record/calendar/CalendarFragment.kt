package com.climus.climeet.presentation.ui.main.record.calendar

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCalendarBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.climus.climeet.presentation.ui.main.record.calendar.DayViewContainer.Companion.selectedDay
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()
        customCalendar()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {

            }
        }
    }

    private fun customCalendar() {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun bind(
                container: DayViewContainer,
                data: CalendarDay
            ) {
                container.textView.text = data.date.dayOfMonth.toString()
            }

            override fun create(view: View) = DayViewContainer(view)
        }

        setupCalendarView()
        setDayOfWeekTitles()
        setupDayBinder()
        setupMonthScrollListener()

    }

    private fun setupCalendarView() {
        // 날짜
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        binding.calendarView.setup(startMonth, endMonth, DayOfWeek.SUNDAY)
        binding.calendarView.scrollToMonth(currentMonth)
    }

    private fun setDayOfWeekTitles() {
        // 요일
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        val titlesContainer = view?.findViewById<ViewGroup>(R.id.container_titles)
        titlesContainer?.children?.map { it as TextView }?.forEachIndexed { index, textView ->
            val dayOfWeek = daysOfWeek[index]
            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            textView.text = title
        }
    }

    private fun setupDayBinder() {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
                container.day = data
                if (data.position == DayPosition.MonthDate) {
                    container.titlesContainer.visibility = View.VISIBLE
                } else {
                    container.titlesContainer.visibility = View.INVISIBLE
                }

                if (data.date == LocalDate.now()) {
                    if (selectedDay == null) {
                        selectedDay = container
                        selectedDay?.select()
                    } else {
                        container.deselect()
                    }
                } else {
                    container.deselect()
                }
            }
        }
    }

    private fun setupMonthScrollListener() {
        // 이전 월로 이동
        binding.ivDatePre.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.minusMonths(1))
            }
        }

        // 다음 월로 이동
        binding.ivDateNext.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.plusMonths(1))
            }
        }

        binding.calendarView.monthScrollListener = { month ->
            // 월이 변경될 때마다 텍스트 뷰를 업데이트
            viewModel.updateSelectedYearMonth(
                month.yearMonth.year.toString(),
                month.yearMonth.month.value.toString()
            )
        }
    }

}


