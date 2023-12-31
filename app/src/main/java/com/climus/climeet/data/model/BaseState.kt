package com.climus.climeet.data.model

import android.util.Log
import com.climus.climeet.presentation.util.Constants.TAG
import com.google.gson.Gson
import retrofit2.Response

sealed class BaseState<out T> {
    data class Success<out T>(val body: T) : BaseState<T>()
    data class Error(val msg: String, val code: String) : BaseState<Nothing>()
}

suspend fun <T> runRemote(block: suspend () -> Response<T>): BaseState<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.let {
                BaseState.Success(it)
            } ?: run {
                BaseState.Error("응답이 비어있습니다", "EMPTY")
            }
        } else {
            val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            BaseState.Error(error.message, error.code)
        }
    } catch (e: Exception) {
        Log.d(TAG, e.message.toString())
        BaseState.Error("네트워크 통신 에러", "EMPTY")
    }
}