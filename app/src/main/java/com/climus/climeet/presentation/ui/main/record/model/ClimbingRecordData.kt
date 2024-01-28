package com.climus.climeet.presentation.ui.main.record.model

data class ClimbingRecordData(
    val id: Long = 0,
    val date : String = "",
    val time : String = "",
    val totalCompleteCount : String = "",
    val totalAttemptCount : String = "",
    val avgDifficulty : String = "",
    val gymName: String = "",
    val gymId : Long = -1
)
