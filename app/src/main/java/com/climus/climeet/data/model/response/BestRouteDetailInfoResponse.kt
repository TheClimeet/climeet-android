package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class BestRouteDetailInfoResponse(
    @SerializedName("ranking")
    val ranking : Int,

    @SerializedName("thisWeekSelectionCount")
    val thisWeekSelectionCount : Int,

    @SerializedName("routeImageUrl")
    val routeImageUrl : String,

    @SerializedName("gymName")
    val gymName : String,

    @SerializedName("sectorName")
    val sectorName : String,

    @SerializedName("level")
    val level : Int
)
