package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName


data class BestFollowGymSimpleResponse(

    @SerializedName("ranking")
    val ranking : Int,

    @SerializedName("profileImageUrl")
    val profileImageUrl : String,

    @SerializedName("gymName")
    val gymName : String,

    @SerializedName("thisWeekFollowerCount")
    val thisWeekFollowerCount : Int,

    @SerializedName("rating")
    val rating : Float,

    @SerializedName("reviewCount")
    val reviewCount : Int

)