package com.climus.climeet.config

import android.content.Intent
import android.util.Log
import com.climus.climeet.BuildConfig
import com.climus.climeet.app.App
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.RefreshTokenResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.MainApi
import com.climus.climeet.presentation.ui.intro.IntroActivity
import com.climus.climeet.presentation.util.Constants.TAG
import com.climus.climeet.presentation.util.Constants.X_ACCESS_TOKEN
import com.climus.climeet.presentation.util.Constants.X_REFRESH_TOKEN
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BearerInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // API 통신중 특정코드 에러 발생 (accessToken 만료)
        if (response.code == 410) {

            var isRefreshed = false
            var accessToken = ""

            runBlocking {

                sharedPreferences.getString(X_REFRESH_TOKEN, null)?.let { refresh ->
                    getNewAccessToken(refresh).let {
                        when (it) {
                            is BaseState.Success -> {

                                sharedPreferences.edit()
                                    .putString(X_ACCESS_TOKEN, it.body.accessToken)
                                    .putString(X_REFRESH_TOKEN, it.body.refreshToken)
                                    .apply()

                                isRefreshed = true
                                accessToken = it.body.accessToken
                            }

                            is BaseState.Error -> {
                                Log.d(TAG,it.msg)
                            }
                        }
                    }
                }
            }

            if (isRefreshed) {

                // 기존 API 재호출
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()

                response.close()

                return chain.proceed(newRequest)
            } else {
                // 해당 특정 에러코드가 그대로 내려간다면, IntroActivity로 이동. 세션 만료 처리
                Log.d(TAG,"세션이 만료되었습니다")
                sharedPreferences.edit()
                    .remove(X_ACCESS_TOKEN)
                    .remove(X_REFRESH_TOKEN)
                    .apply()

                val intent = Intent(App.getContext(), IntroActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                App.getContext().startActivity(intent)
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