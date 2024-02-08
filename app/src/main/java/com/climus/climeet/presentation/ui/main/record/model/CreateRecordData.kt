package com.climus.climeet.presentation.ui.main.record.model

import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import java.time.LocalDate
import java.time.LocalTime

object CreateRecordData {

    val today = LocalDate.now()

    var selectedDate: LocalDate = LocalDate.of(today.year, today.monthValue - 1, today.dayOfMonth)
        private set

    var selectedStartTime: LocalTime = LocalTime.of(11, 0, 1)
        private set

    var selectedEndTime: LocalTime = LocalTime.of(12, 0, 1)
        private set

    var selectedCrag: FollowCrag? = FollowCrag(-1, "", "", "", 0, false)


    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
    }

    fun setSelectedTime(start: LocalTime, end: LocalTime) {
        selectedStartTime = start
        selectedEndTime = end
    }

    fun setSelecetedCrag(crag: FollowCrag) {
        selectedCrag = crag
    }
}