package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class UserHomeGymDetailResponse (

    @SerializedName("gymId")
    val gymId : Long,

    @SerializedName("gymProfileUrl")
    val gymProfileUrl : String,

    @SerializedName("gymName")
    val gymName : String,

    @SerializedName("routeSimpleInfos")
    val routeSimpleInfos : List<RouteSimpleInfo>

)

data class RouteSimpleInfo(
    val routeId: Long,
    val routeImgUrl: String,
    val difficultyName: String,
    val sectorId: Long,
    val sectorName: String
)