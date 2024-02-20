package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class UserFollowingInfoResponse(

    @SerializedName("userId")
    val userId : Long,

    @SerializedName("userName")
    val userName : String,

    @SerializedName("userProfileUrl")
    val profileImgUrl : String,

    @SerializedName("followerCount")
    val followerCount : Int,

    @SerializedName("followingCount")
    val followingCount : Int,

    @SerializedName("isFollower")
    val isFollower : Boolean

)


