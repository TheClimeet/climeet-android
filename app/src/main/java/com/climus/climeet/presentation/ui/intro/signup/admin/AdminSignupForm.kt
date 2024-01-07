package com.climus.climeet.presentation.ui.intro.signup.admin

import android.net.Uri

object AdminSignupForm {

    //todo
    // - 회원가입시, 모든 데이터를 임시 저장후, 최종단계에서 한번의 API 통신으로 회원가입
    // - 회원가입 데이터를 임시 저장하기 위한 싱글톤 Object

    private var cragId: Long = -1L
    private var businessRegistrationUri: Uri? = null

    fun setCragId(id: Long){
        cragId = id
    }

    fun setBusinessRegistrationUri(uri: Uri){
        businessRegistrationUri = uri
    }
}