package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.RefreshTokenResponse

interface AuthRepository {

    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getLoginMode(): String?
    suspend fun getIsFirstApp(): String?

    suspend fun putAccessToken(token: String)
    suspend fun putRefreshToken(token: String)
    suspend fun putLoginMode(mode: String)
    suspend fun putIsFirstApp()

    suspend fun deleteAccessToken()
    suspend fun deleteRefreshToken()
    suspend fun deleteLoginMode()
    suspend fun refreshToken(refreshToken: String): BaseState<RefreshTokenResponse>
}