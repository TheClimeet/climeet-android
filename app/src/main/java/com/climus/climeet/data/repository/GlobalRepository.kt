package com.climus.climeet.data.repository

import android.net.Uri
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.remote.GlobalApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

interface GlobalRepository {
    suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse>
}