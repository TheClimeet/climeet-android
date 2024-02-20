package com.climus.climeet.data.model.response

data class GymTimeBestClimberResponse (
    val totalTime: String,
    val userId: Long,
    val profileName : String,
    val profileImageUrl : String,
    val ranking : Int
)