package com.climus.climeet.data.model.response

class GymCompleteBestClimberResponse (
    val totalCompletedCount : Int,
    val userId: Long,
    val profileName : String,
    val profileImageUrl : String,
    val ranking : Int
)