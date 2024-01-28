package com.climus.climeet.presentation.ui.main.record.createclimbingrecord

import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import java.time.LocalDate

object CreateRecordData {

    var selectedDate: LocalDate = LocalDate.of(9999,12,31)
        private set

    var selectedCrag: FollowCrag? = FollowCrag(-1, "", "", "", 0, false)


    fun setSelectedDate(date: LocalDate){
        selectedDate = date
    }

    fun setSelecetedCrag(crag: FollowCrag){
        selectedCrag = crag
    }
}