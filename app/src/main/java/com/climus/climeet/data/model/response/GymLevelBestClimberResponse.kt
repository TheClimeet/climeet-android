package com.climus.climeet.data.model.response

data class GymLevelBestClimberResponse (
    val userId: Long,
    val profileName : String,
    val profileImageUrl : String,
    val ranking : Int,
    val highDifficulty: Int,
    val highDifficultyCount: Int,
    val climeetDifficultyName: String,
    val gymDifficultyName: String,
    val gymDifficultyColor: String
)