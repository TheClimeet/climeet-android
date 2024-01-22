package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import retrofit2.Response

interface IntroRepository {

    suspend fun climerSignUp(
        provider : String,
        accessToken : String,
        signUpRequest : ClimerSignupRequest
    ) : Response<ClimerSignupResponse>

}