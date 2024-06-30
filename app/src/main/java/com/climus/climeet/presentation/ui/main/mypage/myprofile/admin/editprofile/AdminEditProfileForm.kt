package com.climus.climeet.presentation.ui.main.mypage.myprofile.admin.editprofile

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AdminEditProfileForm {

    private lateinit var backgroundImg: String
    private lateinit var profileImg: String

    private var nameUpdated = false

    private val _profileUriState = MutableStateFlow("")
    val profileUriState: StateFlow<String> = _profileUriState

    private val _backgroundUriState = MutableStateFlow("")
    val backgroundUriState: StateFlow<String> = _backgroundUriState

    var isBackgroundChange = false

    fun setProfileImage(image: String) {
        profileImg = image
        _profileUriState.value = image

        Log.d("form", "프로필 이미지 uri 저장 : $image")
    }

    fun setBackgroundImage(image: String) {
        backgroundImg = image
        _backgroundUriState.value = image
        isBackgroundChange = true

        Log.d("form", "배경 이미지 uri 저장 : $image")
    }

    fun setNameUpdatedState(state: Boolean) {
        nameUpdated = state
    }

    fun getProfileImagePath(): String {
        return profileImg
    }

    fun getBackgroundPath(): String {
        return backgroundImg
    }

    fun getNameUpdatedState(): Boolean {
        return nameUpdated
    }

    fun resetState() {
        _profileUriState.value = ""
        _backgroundUriState.value = ""
        nameUpdated = false
    }
}