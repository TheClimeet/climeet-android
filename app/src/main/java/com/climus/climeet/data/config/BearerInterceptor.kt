package com.climus.climeet.data.config

import android.util.Log
import com.climus.climeet.BuildConfig
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.RefreshTokenResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.MainApi
import com.climus.climeet.presentation.util.Constants.TAG
import com.kakao.sdk.common.Constants.AUTHORIZATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class BearerInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        var newAccessToken: String? = null

        // API 통신중 특정코드 에러 발생 (accessToken 만료)
        if (response.code == 410) {

            runBlocking {

                val refreshToken = dataStoreManager.getRefreshToken().first()
                refreshToken?.let { token ->
                    getNewAccessToken(token).let {
                        when (it) {
                            is BaseState.Success -> {

                                dataStoreManager.putAccessToken(it.body.accessToken)
                                dataStoreManager.putRefreshToken(it.body.refreshToken)

                                newAccessToken = it.body.accessToken
                            }

                            is BaseState.Error -> {
                                dataStoreManager.deleteAccessToken()
                                dataStoreManager.deleteRefreshToken()
                                Log.d(TAG, it.msg)
                            }
                        }
                    }
                }
            }

            newAccessToken?.let {
                val newRequest = originalRequest.newBuilder()
                    .addHeader(AUTHORIZATION, it)
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return response
    }

    private suspend fun getNewAccessToken(refreshToken: String): BaseState<RefreshTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_DEV_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val api = retrofit.create(MainApi::class.java)
        return runRemote {
            api.refreshToken(
                refreshToken
            )
        }
    }
}