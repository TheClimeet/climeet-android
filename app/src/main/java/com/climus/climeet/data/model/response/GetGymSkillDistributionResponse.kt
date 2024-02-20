package com.climus.climeet.data.model.response

data class GetGymSkillDistributionResponse (
    val gymId: Long,
    val difficulty: Int,
    val gymDifficultyName: String,
    val gymDifficultyColor: String,
    val percentage: Int
)