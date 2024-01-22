package com.climus.climeet.data.repository

import android.util.Log
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.GlobalApi
import com.climus.climeet.presentation.ui.intro.signup.climer.ClimerSignupForm
import com.climus.climeet.presentation.util.Constants.TAG
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class GlobalRepositoryImpl @Inject constructor(
    private val globalApi: GlobalApi
) : GlobalRepository {
    override suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse> {
        return when(val result = runRemote { globalApi.uploadImage(image) }) {
            is BaseState.Error -> {
                Log.d(TAG, "${result.code}")
                result

            }
            is BaseState.Success -> {
                Log.d(TAG, "${result.body.imgUrl}")
                ClimerSignupForm.setImageUri(result.body.imgUrl)
                result
            }
        }
    }


}