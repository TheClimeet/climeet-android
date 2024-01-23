package com.climus.climeet.presentation.ui.intro.signup.admin

import android.net.Uri

object AdminSignupForm {

    //todo
    // - 회원가입시, 모든 데이터를 임시 저장후, 최종단계에서 한번의 API 통신으로 회원가입
    // - 회원가입 데이터를 임시 저장하기 위한 싱글톤 Object

    private var cragId: Long = -1L

    var cragName: String = ""
        private set

    private var businessRegistrationUri: Uri? = null

    private var id = ""
    private var pw = ""
    private var name = ""
    private var phoneNum = ""
    private var email = ""
    private var backgroundImage: Uri? = null
    private var services: List<String> = emptyList()    // service 형식 고려
    private var alarm = false

    fun setCragId(id: Long) {
        cragId = id
    }

    fun setCragName(name: String) {
        cragName = name
    }

    fun setBusinessRegistrationUri(uri: Uri) {
        businessRegistrationUri = uri
    }

    fun setId(data: String) {
        id = data
    }

    fun setPw(data: String) {
        pw = data
    }

    fun setName(data: String) {
        name = data
    }

    fun setPhone(data: String) {
        phoneNum = data
    }

    fun setEmail(data: String) {
        email = data
    }

    fun setBackgroundImg(uri: Uri) {
        backgroundImage = uri
    }

    fun setSelectedServices(data: List<String>) {
        services = data
    }

    fun setAlarm(data: Boolean) {
        alarm = data
    }
}