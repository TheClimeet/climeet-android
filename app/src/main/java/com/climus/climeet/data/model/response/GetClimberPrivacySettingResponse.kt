package com.climus.climeet.data.model.response

data class GetClimberPrivacySettingResponse(
    val shortsPublic: Boolean,
    val homeGymPublic: Boolean,
    val averageCompletionRatePublic: Boolean,
    val averageCompletionLevelPublic: Boolean
)
