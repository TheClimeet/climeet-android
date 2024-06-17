package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody

object ClimberEditProfileForm {

    private lateinit var profileImg: MultipartBody.Part

    private val _imageUriState = MutableStateFlow("")
    val imageUriState: StateFlow<String> = _imageUriState

    fun setProfileImage(image: MultipartBody.Part) {
        profileImg = image
        _imageUriState.value = image.toString()

        Log.d("form", "이미지 파일 저장 : 이름=${image.headers?.get("Content-Disposition")}, " +
                "타입=${image.body.contentType()}, 크기=${image.body.contentLength()}")
    }

    fun getProfileImagePath(): MultipartBody.Part {
        return profileImg
    }

    fun resetState() {
        _imageUriState.value = ""
    }
}