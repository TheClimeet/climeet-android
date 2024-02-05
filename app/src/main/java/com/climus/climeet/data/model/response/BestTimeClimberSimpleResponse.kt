package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class BestTimeClimberSimpleResponse(
    @SerializedName("ranking")
    val ranking : Int,

    @SerializedName("profileImageUrl")
    val profileImageUrl : String,

    @SerializedName("profileName")
    val profileName : String,

    @SerializedName("thisWeekTotalClimbingTime")
    val thisWeekTotalClimbingTime : Long
)
