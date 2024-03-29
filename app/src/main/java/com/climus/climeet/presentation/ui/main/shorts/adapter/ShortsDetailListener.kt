package com.climus.climeet.presentation.ui.main.shorts.adapter

interface ShortsDetailListener {
    fun navigateToProfileDetail(userId: Long)
    fun navigateToRouteShorts(routeId: Long)
    fun showCommentDialog(shortsId: Long, profileImgUrl: String?)
}