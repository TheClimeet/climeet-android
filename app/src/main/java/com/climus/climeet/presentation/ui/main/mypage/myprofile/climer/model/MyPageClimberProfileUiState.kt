package com.climus.climeet.presentation.ui.main.mypage.myprofile.climer.model

data class MyPageClimberProfileUiState(
    val shortsPublic: Boolean = true,
    val homeGymPublic: Boolean = true,
    val avgCompletionRatePublic: Boolean = true,
    val avgCompletionLevelPublic: Boolean = true
)