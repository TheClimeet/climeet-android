package com.climus.climeet.presentation.ui.main.global.gymprofile.model

data class GymReview(
    val profileImageUrl: String,
    val profileName: String,
    val rating: Float,
    val content: String,
    val updatedAt: String
)