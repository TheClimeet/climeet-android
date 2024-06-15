package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ClimberEditProfileForm {

    private var nickname = ""
    private var profileImgUrl = ""
    private var imageUpdateState = false

    private val _imageUriState = MutableStateFlow("")
    val imageUriState: StateFlow<String> = _imageUriState


    fun setNickName(name: String) {
        nickname = name
        Log.d("form", "닉네임 저장 : $nickname")
    }

    fun setProfileImageUri(uri: String) {
        profileImgUrl = uri
        imageUpdateState = true
        _imageUriState.value = uri
        Log.d("form", "이미지 uri 저장 : $uri")
    }

    fun resetState() {
        nickname = ""
        profileImgUrl = ""
        imageUpdateState = false
        _imageUriState.value = ""
    }
}