package com.climus.climeet.data.model.response

data class ClimerAuthResponse(
    val socialType: SocialType,
    val accessToken: String,
    val refreshToken: String?,
    val responseType: String
)

enum class SocialType { KAKAO, NAVER }
