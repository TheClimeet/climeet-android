package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import java.time.LocalDate

object CreateRecordData {

    var selectedDate: LocalDate = LocalDate.of(9999,12,31)
        private set

    fun setSelectedDate(date: LocalDate){
        selectedDate = date
    }
}