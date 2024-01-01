package com.climus.climeet.presentation.ui.intro.signup.climer

object ClimerSignupForm {

    //todo
    // - 회원가입시, 모든 데이터를 임시 저장후, 최종단계에서 한번의 API 통신으로 회원가입
    // - 회원가입 데이터를 임시 저장하기 위한 싱글톤 Object

    var token = ""
    private set

    var socialType = ""
    private set

    fun setToken(data: String){
        token = data
    }

    fun setSocialType(data: String){
        socialType = data
    }

}