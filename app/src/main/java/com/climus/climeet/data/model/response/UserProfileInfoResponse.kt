package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class UserProfileInfoResponse(

    @SerializedName("userId")
    val userId : Long,

    @SerializedName("userName")
    val userName : String,

    @SerializedName("profileImgUrl")
    val profileImgUrl : String,

    @SerializedName("followerCount")
    val followerCount : Int,

    @SerializedName("followingCount")
    val followingCount : Int,

    @SerializedName("isManager")
    val isManager : Boolean

)
