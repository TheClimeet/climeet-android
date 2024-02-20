package com.climus.climeet.presentation.ui.main.shorts.adapter

interface ShortsDetailListener {
    fun navigateToClimberProfile(userId: Long)
    fun navigateToGymProfile(gymId: Long)
    fun navigateToRouteShorts(routeId: Long)
    fun showCommentDialog(shortsId: Long, profileImgUrl: String?)
}