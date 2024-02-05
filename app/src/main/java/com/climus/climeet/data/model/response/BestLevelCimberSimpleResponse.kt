package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class BestLevelCimberSimpleResponse(
    @SerializedName("ranking")
    val ranking : Int,

    @SerializedName("profileImageUrl")
    val profileImageUrl : String,

    @SerializedName("profileName")
    val profileName : String,

    @SerializedName("thisWeekHighDifficulty")
    val thisWeekHighDifficulty : Int
)
