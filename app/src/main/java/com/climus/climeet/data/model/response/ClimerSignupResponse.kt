package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class ClimerSignupResponse(
    @SerializedName("socialType")
    val socialType: SocialType,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)

enum class SocialType { KAKAO, NAVER }
