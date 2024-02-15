package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class UserHomeGymSimpleResponse (

    @SerializedName("gymId")
    val gymId : Long,

    @SerializedName("gymProfileUrl")
    val gymProfileUrl : String,

    @SerializedName("gymName")
    val gymName : String,

)