package com.climus.climeet.data.remote

import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IntroApi {
    @POST("/climber/signup/{provider}/{accessToken}")
    suspend fun signUpClimer(
        @Path("provider") provider: String,
        @Path("accessToken") accessToken: String,
        @Body signUpRequest: ClimerSignupRequest
    ): Response<ClimerSignupResponse>

}