package com.climus.climeet.presentation.ui.main.record.calendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.climus.climeet.R
import com.climus.climeet.databinding.FragmentCalendarBinding
import com.climus.climeet.presentation.base.BaseFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.viewForSelectYearMonth.bringToFront()

        initEventObserve()
        customCalendar()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {

            }
        }
    }

    private fun customCalendar(){
        with(binding){
            // 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
            calendarView.setTitleFormatter(TitleFormatter { day ->
                // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
                // 때문에 MaterialCalendarView에서 연/월 보여주기를 커스텀하려면 CalendarDay 객체의 getDate()로 연/월을 구한 다음 LocalDate 객체에 넣어서
                // LocalDate로 변환하는 처리가 필요하다
                val inputText: org.threeten.bp.LocalDate = day.date
                val calendarHeaderElements =
                    inputText.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val calendarHeaderBuilder = StringBuilder()
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                    .append(".")
                    .append(calendarHeaderElements[1])
                calendarHeaderBuilder.toString()
            })

            // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
            val decorator = DayDecorator(requireContext())
            calendarView.addDecorators(decorator)

            calendarView.setOnDateChangedListener { _, date, _ ->
                decorator.setSelectedDay(date)
                calendarView.invalidateDecorators()
            }

        }
    }
}

/* 선택된 요일의 background를 설정하는 Decorator 클래스 */
private class DayDecorator(context: Context?) : DayViewDecorator {
    private val drawable: Drawable?
    private var selectedDay: CalendarDay? = null

    init {
        drawable = ContextCompat.getDrawable(context!!, R.drawable.rect_mainfill_nostroke_5radius)
    }

    fun setSelectedDay(day: CalendarDay) {
        selectedDay = day
    }

    // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day != null && day == selectedDay
    }

    // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
        //            view.addSpan(new StyleSpan(Typeface.BOLD));   // 달력 안의 모든 숫자들이 볼드 처리됨
    }
}