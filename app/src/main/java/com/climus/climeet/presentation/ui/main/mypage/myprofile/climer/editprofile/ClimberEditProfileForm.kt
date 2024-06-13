package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.editprofile

object ClimberEditProfileForm {

    private var nickname = ""
    private var profileImgUrl = ""


    fun setNickName(name: String) {
        nickname = name
    }

    fun setProfileImageUri(uri: String) {
        profileImgUrl = uri
    }
}