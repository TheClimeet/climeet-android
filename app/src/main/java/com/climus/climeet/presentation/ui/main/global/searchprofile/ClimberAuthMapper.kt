package com.climus.climeet.presentation.ui.main.global.searchprofile

import com.climus.climeet.data.model.response.ClimberDetailInfoItem
import com.climus.climeet.presentation.ui.main.global.searchprofile.model.FollowClimber

fun ClimberDetailInfoItem.toFollowClimber(
    keyword: String,
) = FollowClimber(
    userId = userId,
    climberName = climberName,
    profileImageUrl = profileImgUrl,
    keyword = keyword,
    followerCount = followerCount,
    isFollowing = isFollower,
)