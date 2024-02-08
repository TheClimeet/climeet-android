package com.climus.climeet.presentation.ui.main.record.calendar

import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.presentation.ui.main.record.model.ClimbingRecordData

fun GetSelectDateRecordResponse.toClimbingRecordData(
    gymName: String,
    gymProfile: String
) = ClimbingRecordData(
    id = climbingRecordId,
    date = date,
    time = getFormattedTime(),
    totalCompleteCount = totalCompletedCount.toString(),
    totalAttemptCount = totalAttemptCount.toString(),
    avgDifficulty = "V${avgDifficulty}",
    gymId = gymId,
    gymProfile = gymProfile,
    gymName = gymName
)