package com.climus.climeet.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.climus.climeet.data.config.DataStoreManager
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.RefreshTokenResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.AuthApi
import com.kakao.sdk.auth.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val api: AuthApi
) :
    AuthRepository {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey(Constants.ACCESS_TOKEN)
        private val REFRESH_TOKEN_KEY = stringPreferencesKey(Constants.REFRESH_TOKEN)
        private val LOGIN_MODE =
            stringPreferencesKey(com.climus.climeet.presentation.util.Constants.X_MODE)
        private val IS_FIRST_APP =
            stringPreferencesKey(com.climus.climeet.presentation.util.Constants.IS_FIRST_APP)
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }.first()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }.first()
    }

    override suspend fun getLoginMode(): String? {
        return dataStore.data.map { prefs ->
            prefs[LOGIN_MODE]
        }.first()
    }

    override suspend fun getIsFirstApp(): String? {
        return dataStore.data.map { pref ->
            pref[IS_FIRST_APP]
        }.first()
    }

    override suspend fun putIsFirstApp() {
        dataStore.edit { prefs ->
            prefs[IS_FIRST_APP] = ""
        }
    }

    override suspend fun putAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    override suspend fun putRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = token
        }
    }

    override suspend fun putLoginMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[LOGIN_MODE] = mode
        }
    }

    override suspend fun deleteAccessToken() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }

    override suspend fun deleteRefreshToken() {
        dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun deleteLoginMode() {
        dataStore.edit { prefs ->
            prefs.remove(LOGIN_MODE)
        }
    }


    override suspend fun refreshToken(refreshToken: String): BaseState<RefreshTokenResponse> =
        runRemote {
            api.refreshToken(refreshToken)
        }
}