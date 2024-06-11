package com.climus.climeet.presentation.ui.main.global.gymprofile

import java.time.LocalDate

object GymProfileData {
    private val today = LocalDate.now()

    var selectedDate: LocalDate = LocalDate.of(today.year, today.monthValue, today.dayOfMonth)
        private set

    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
    }

}