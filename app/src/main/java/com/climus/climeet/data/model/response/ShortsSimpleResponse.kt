package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class ShortsSimpleResponse(
    @SerializedName("shortsId")
    val shortsId : Long,

    @SerializedName("thumbnailImageUrl")
    val thumbnailImageUrl : String,

    @SerializedName("gymName")
    val gymName : String,

    @SerializedName("difficulty")
    val difficulty : Int,

    @SerializedName("thisWeekTotalClimbingTime")
    val thisWeekTotalClimbingTime : Long
)
