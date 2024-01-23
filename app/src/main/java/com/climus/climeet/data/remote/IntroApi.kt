package com.climus.climeet.data.remote

import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IntroApi {

    @POST("/climber/login")
    suspend fun climerLogin(
        @Query("provider") provider: String,
        @Header("Authorization") accessToken: String,
        @Body signUpRequest: ClimerSignupRequest
    ): Response<ClimerSignupResponse>


}