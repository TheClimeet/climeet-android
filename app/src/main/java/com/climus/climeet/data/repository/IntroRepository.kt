package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.request.FcmTokenRequest
import com.climus.climeet.data.model.request.ManagerLoginRequest
import com.climus.climeet.data.model.request.ManagerSignUpRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import com.climus.climeet.data.model.response.ManagerLoginResponse

interface IntroRepository {

    suspend fun climerSignUp(
        provider: String,
        accessToken: String,
        body: ClimerSignupRequest
    ): BaseState<ClimerSignupResponse>

    suspend fun managerSignUp(
        body: ManagerSignUpRequest
    ): BaseState<Unit>

    suspend fun climerLogin(
        provider: String,
        accessToken: String,
    ): BaseState<ClimerSignupResponse>

    suspend fun managerLogin(
        body : ManagerLoginRequest
    ): BaseState<ManagerLoginResponse>


    suspend fun managerIdCheck(
        loginId: String
    ): BaseState<Boolean>

    suspend fun managerGymNameCheck(
        gymName: String
    ): BaseState<Boolean>

    suspend fun climberNickNameCheck(
        nickName: String
    ): BaseState<Boolean>

    suspend fun patchFcmToken(body: FcmTokenRequest): BaseState<Unit>
}