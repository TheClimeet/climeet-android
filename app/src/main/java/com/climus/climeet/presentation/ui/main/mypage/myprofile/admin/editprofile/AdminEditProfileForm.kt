package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody

object AdminEditProfileForm {

    private lateinit var backgroundImg: MultipartBody.Part
    private lateinit var profileImg: MultipartBody.Part

    private val _profileUriState = MutableStateFlow("")
    val profileUriState: StateFlow<String> = _profileUriState

    private val _backgroundUriState = MutableStateFlow("")
    val backgroundUriState: StateFlow<String> = _backgroundUriState

    fun setProfileImage(image: MultipartBody.Part) {
        profileImg = image
        _profileUriState.value = image.toString()

        Log.d("form", "프로필 이미지 파일 저장 : 이름=${image.headers?.get("Content-Disposition")}, " +
                "타입=${image.body.contentType()}, 크기=${image.body.contentLength()}")
    }

    fun setBackgroundImage(image: MultipartBody.Part) {
        backgroundImg = image
        _backgroundUriState.value = image.toString()

        Log.d("form", "배경 이미지 파일 저장 : 이름=${image.headers?.get("Content-Disposition")}, " +
                "타입=${image.body.contentType()}, 크기=${image.body.contentLength()}")
    }

    fun getProfileImagePath(): MultipartBody.Part {
        return profileImg
    }

    fun getBackgroundPath(): MultipartBody.Part {
        return backgroundImg
    }

    fun resetState() {
        _profileUriState.value = ""
        _backgroundUriState.value = ""
    }
}