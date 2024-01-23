package com.climus.climeet.data.remote

import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.request.ManagerLoginRequest
import com.climus.climeet.data.model.request.ManagerSignUpRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import com.climus.climeet.data.model.response.ManagerLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IntroApi {

    @POST("/climber/login")
    suspend fun climerSignUp(
        @Query("provider") provider: String,
        @Header("Authorization") accessToken: String,
        @Body params: ClimerSignupRequest
    ): Response<ClimerSignupResponse>

    @POST("/manager/signup")
    suspend fun managerSignUp(
        @Body params: ManagerSignUpRequest
    ): Response<Unit>

    @POST("/climber/login")
    suspend fun climerLogin(
        @Query("provider") provider: String,
        @Header("Authorization") accessToken: String,
    ): Response<ClimerSignupResponse>

    @POST("/manager/login")
    suspend fun managerLogin(
        @Body params: ManagerLoginRequest
    ): Response<ManagerLoginResponse>

    @GET("/manager/check-id/{loginId}")
    suspend fun managerIdCheck(
        @Path("loginId") loginId: String
    ): Response<Boolean>

    @GET("/manager/isRegistered/{gymName}")
    suspend fun managerGymNameCheck(
        @Path("gymName") gymName: String
    ): Response<Boolean>


}