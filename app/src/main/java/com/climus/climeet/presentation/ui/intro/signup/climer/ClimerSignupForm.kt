package com.climus.climeet.presentation.ui.intro.signup.climer

import android.net.Uri

object ClimerSignupForm {

    //todo
    // - 회원가입시, 모든 데이터를 임시 저장후, 최종단계에서 한번의 API 통신으로 회원가입
    // - 회원가입 데이터를 임시 저장하기 위한 싱글톤 Object

    var token = ""
        private set

    var socialType = ""
        private set

    var nickName = ""
        private set

    var imageUri: Uri? = null
        private set

    var level = 0
        private set

    var howToKnow : Int = 0
        private set

    var noticePermission : Boolean = false
        private set

    fun setToken(data: String) {
        token = data
    }

    fun setSocialType(data: String) {
        socialType = data
    }

    fun setNickName(data: String) {
        nickName = data
    }

    fun setImageUri(uri: Uri?) {
        imageUri = uri
    }

    fun setLevel(climerLevel: Int) {
        level = climerLevel
    }

    fun setHowToKnow(path: Int) {
        howToKnow = path
    }

    fun setNotice(permit: Boolean) {
        noticePermission = permit
    }
}