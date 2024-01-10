package com.climus.climeet.presentation.ui.intro.signup.admin

import android.net.Uri

import com.climus.climeet.presentation.ui.intro.signup.admin.model.ServiceUiData

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
    var id = ""
        private set
    var pw = ""
        private set
    var name = ""
        private set
    var phoneNum = ""
        private set
    var email = ""
        private set
    var backgroundImage: Uri? = null
        private set
    var services: List<ServiceUiData> = emptyList()    // service 형식 고려
        private set
    var alarm = false
        private set

    fun setId(data: String){
        id = data
    }
    fun setPw(data: String){
        pw = data
    }
    fun setName(data: String){
        name = data
    }
    fun setPhone(data: String){
        phoneNum = data
    }
    fun setEmail(data: String){
        email = data
    }
    fun setBackgroundImg(uri: Uri) {
        backgroundImage = uri
    }
    fun setSelectedServices(data: List<ServiceUiData>) {
        services = data
    }
    fun setAlarm(data: Boolean){
        alarm = data
    }
}