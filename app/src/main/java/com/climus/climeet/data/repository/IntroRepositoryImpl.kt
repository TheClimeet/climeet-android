package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.request.ManagerLoginRequest
import com.climus.climeet.data.model.request.ManagerSignUpRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import com.climus.climeet.data.model.response.ManagerLoginResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.IntroApi
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository {

    override suspend fun climerSignUp(
        provider: String,
        accessToken: String,
        body: ClimerSignupRequest
    ): BaseState<ClimerSignupResponse> = runRemote { api.climerSignUp(provider, accessToken, body) }

    override suspend fun managerSignUp(body: ManagerSignUpRequest): BaseState<Unit> = runRemote { api.managerSignUp(body) }

    override suspend fun climerLogin(provider: String, accessToken: String): BaseState<ClimerSignupResponse> = runRemote { api.climerLogin(provider, accessToken) }

    override suspend fun managerLogin(body: ManagerLoginRequest): BaseState<ManagerLoginResponse> = runRemote { api.managerLogin(body) }

    override suspend fun managerIdCheck(loginId: String): BaseState<Boolean> = runRemote { api.managerIdCheck(loginId) }

    override suspend fun managerGymNameCheck(gymName: String): BaseState<Boolean> = runRemote { api.managerGymNameCheck(gymName) }

    override suspend fun climberNickNameCheck(nickName: String): BaseState<Boolean> = runRemote { api.climberNickNameCheck(nickName) }
}