package com.climus.climeet.data.config


import com.kakao.sdk.common.Constants.AUTHORIZATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(private val dataStoreManager: DataStoreManager) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val jwt = runBlocking {
            dataStoreManager.getAccessToken().first()
        }

        jwt?.takeIf { it.isNotEmpty() }?.let {
            builder.addHeader(AUTHORIZATION, "Bearer $it")
        }

        return chain.proceed(builder.build())
    }
}