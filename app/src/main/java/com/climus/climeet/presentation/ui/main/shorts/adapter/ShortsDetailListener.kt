package com.climus.climeet.presentation.ui.main.shorts.adapter

interface ShortsDetailListener {
    fun showShareDialog()
    fun navigateToProfileDetail(userId: Long)
    fun navigateToRouteShorts(routeId: Long)
    fun showCommentDialog(shortsId: Long)
}