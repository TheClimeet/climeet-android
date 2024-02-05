package com.climus.climeet.presentation.ui.main.shorts.adapter

interface ShortsDetailListener {
    fun onClickLikeBtn(shortsId: Long)
    fun onClickBookMarkBtn(shortsId: Long)
    fun showShareDialog()
    fun navigateToProfileDetail(userId: Long)
    fun navigateToSectorShorts(sectorId: Long)
    fun showCommentDialog(shortsId: Long)
    fun onClickDescription()
}