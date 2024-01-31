package com.climus.climeet.presentation.ui.intro.signup.admin

import com.climus.climeet.data.model.request.ManagerSignUpRequest

object AdminSignupForm {

    private var cragId: Long = -1L

    var cragName: String = ""
        private set

    private var id = ""
    private var pw = ""
    private var name = ""
    private var phoneNum = ""
    private var email = ""
    private var services: List<String> = emptyList()    // service 형식 고려
    private var alarm = false
    private var backgroundImgUrl = ""
    private var businessRegistrationUrl = ""

    fun setCragId(id: Long) {
        cragId = id
    }

    fun setCragName(name: String) {
        cragName = name
    }

    fun setBusinessRegistrationUrl(url: String) {
        businessRegistrationUrl = url
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

    fun setBackgroundUrl(url: String) {
        backgroundImgUrl = url
    }

    fun setSelectedServices(data: List<String>) {
        services = data
    }

    fun setAlarm(data: Boolean) {
        alarm = data
    }

    fun getSignupRequest(): ManagerSignUpRequest = ManagerSignUpRequest(
        gymId = cragId,
        loginId = id,
        password = pw,
        name = name,
        phoneNumber = phoneNum,
        email = email,
        backGroundImageUri = backgroundImgUrl,
        provideServiceList = services,
        isAllowAdNotification = alarm,
        isAllowCommentNotification = alarm,
        isAllowFollowNotification = alarm,
        isAllowLikeNotification = alarm
    )
}