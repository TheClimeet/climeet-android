package com.climus.climeet.data.model.response

data class MyPageClimberProfilePrivacyResponse (
    val shortsPublic: Boolean = true,
    val homeGymPublic: Boolean = true,
    val averageCompletionRatePublic: Boolean = true,
    val averageCompletionLevelPublic: Boolean = true
)