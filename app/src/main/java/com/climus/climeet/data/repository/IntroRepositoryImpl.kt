package com.climus.climeet.data.repository

import com.climus.climeet.presentation.util.Constants.TAG
import android.util.Log
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.ClimerSignupRequest
import com.climus.climeet.data.model.response.ClimerSignupResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.IntroApi
import retrofit2.Response
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val introApi : IntroApi
) : IntroRepository {

    override suspend fun climerSignUp(
        provider : String,
        accessToken : String,
        signUpRequest : ClimerSignupRequest
    ) : Response<ClimerSignupResponse> {
        return introApi.climerLogin(provider, accessToken, signUpRequest)
    }

}