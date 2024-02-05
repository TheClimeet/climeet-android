package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class BestRouteSimpleResponse(
    @SerializedName("ranking")
    val ranking : Int,

    @SerializedName("routeImageUrl")
    val routeImageUrl : String,

    @SerializedName("gymName")
    val gymName : String,

    @SerializedName("thisWeekSelectionCount")
    val thisWeekSelectionCount : Int,

    @SerializedName("sectorName")
    val sectorName : String,

    @SerializedName("reviewCount")
    val level : Int
)
