package com.climus.climeet.data.remote

import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GlobalApi {

    @Multipart
    @POST("/file")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<UploadImgResponse>

}