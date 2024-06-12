package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.AuthRequest
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.request.FcmTokenRequest
import com.climus.climeet.data.model.request.ManagerLoginRequest
import com.climus.climeet.data.model.request.ManagerSignUpRequest
import com.climus.climeet.data.model.response.ClimerAuthResponse
import com.climus.climeet.data.model.response.ManagerLoginResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.IntroApi
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository {

    override suspend fun climerSignUp(
        body: ClimerSignupRequest
    ): BaseState<ClimerAuthResponse> = runRemote {
        api.climerSignUp(body)
    }

    override suspend fun climerLogin(
        provider: String,
        body: AuthRequest
    ): BaseState<ClimerAuthResponse> = runRemote {
        api.climerLogin(provider, body)
    }

    override suspend fun managerSignUp(body: ManagerSignUpRequest): BaseState<Unit> =
        runRemote { api.managerSignUp(body) }

    override suspend fun managerLogin(body: ManagerLoginRequest): BaseState<ManagerLoginResponse> =
        runRemote { api.managerLogin(body) }

    override suspend fun managerIdCheck(loginId: String): BaseState<Boolean> =
        runRemote { api.managerIdCheck(loginId) }

    override suspend fun managerGymNameCheck(gymName: String): BaseState<Boolean> =
        runRemote { api.managerGymNameCheck(gymName) }

    override suspend fun climberNickNameCheck(nickName: String): BaseState<Boolean> =
        runRemote { api.climberNickNameCheck(nickName) }

    override suspend fun patchFcmToken(body: FcmTokenRequest): BaseState<Unit> =
        runRemote { api.patchFcmToken(body) }
}