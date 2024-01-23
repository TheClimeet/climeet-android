package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse

interface IntroRepository {

    suspend fun climerAuth(
        provider: String,
        accessToken: String,
        signUpRequest: ClimerSignupRequest
    ): BaseState<ClimerSignupResponse>

}