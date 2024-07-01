package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.model

import java.time.LocalDate

object MyPageAdminRouteData {

    private val today = LocalDate.now()

    var selectedDate: LocalDate = LocalDate.of(today.year, today.monthValue, today.dayOfMonth)
        private set

    fun setSelectedDate(date: LocalDate) {
        selectedDate = date
    }

}