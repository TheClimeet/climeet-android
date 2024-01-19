package com.climus.climeet.data.repository

import android.net.Uri
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.remote.GlobalApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class GlobalRepository @Inject constructor(private val globalApi: GlobalApi) {
    suspend fun uploadImage(image: RequestBody): Response<UploadImgResponse> {
        val part = MultipartBody.Part.createFormData("file", "image.jpg", image)
        return globalApi.uploadImage(part)
    }
}