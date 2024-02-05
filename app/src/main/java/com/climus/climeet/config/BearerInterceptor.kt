package com.climus.climeet.config

import android.content.Intent
import android.util.Log
import com.climus.climeet.BuildConfig
import com.climus.climeet.app.App
import com.climus.climeet.app.App.Companion.sharedPreferences
import com.climus.climeet.data.model.ErrorResponse
import com.climus.climeet.data.remote.MainApi
import com.climus.climeet.presentation.ui.intro.IntroActivity
import com.climus.climeet.presentation.util.Constants.TAG
import com.climus.climeet.presentation.util.Constants.X_ACCESS_TOKEN
import com.climus.climeet.presentation.util.Constants.X_REFRESH_TOKEN
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
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
                    Log.d(TAG, refresh)

                    val result = Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_DEV_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MainApi::class.java).refreshToken(refresh)

                    if (result.isSuccessful) {
                        Log.d(TAG, "리프래시 성공")
                        result.body()?.let { body ->
                            Log.d(TAG, body.accessToken)
                            // refresh 성공시 로컬에 저장
                            sharedPreferences.edit()
                                .putString(X_ACCESS_TOKEN, body.accessToken)
                                .putString(X_REFRESH_TOKEN, body.refreshToken)
                                .apply()

                            isRefreshed = true
                            accessToken = body.accessToken
                        }
                    } else {
                        val error =
                            Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
//                        Log.d(TAG, error.message)
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
}