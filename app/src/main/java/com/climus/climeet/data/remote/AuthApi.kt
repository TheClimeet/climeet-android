package com.climus.climeet.data.remote

import com.climus.climeet.data.model.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("refresh-token")
    suspend fun refreshToken(
        @Query("refreshToken") refreshToken: String
    ): Response<RefreshTokenResponse>

}