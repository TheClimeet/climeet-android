package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.IntroApi
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository {

    override suspend fun climerAuth(
        provider: String,
        accessToken: String,
        signUpRequest: ClimerSignupRequest
    ): BaseState<ClimerSignupResponse> = runRemote { api.climerLogin(provider, accessToken, signUpRequest) }


}