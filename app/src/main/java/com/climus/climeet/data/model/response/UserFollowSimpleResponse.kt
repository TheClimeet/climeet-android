package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class UserFollowSimpleResponse(
    @SerializedName("userId")
    val userId : Long,

    @SerializedName("userName")
    val userName : String,

    @SerializedName("userProfileUrl")
    val userProfileUrl : String,

    @SerializedName("followerCount")
    var followerCount : Long,
)
