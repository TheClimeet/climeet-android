package com.climus.climeet.data.model.response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
