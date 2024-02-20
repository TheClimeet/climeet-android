package com.climus.climeet.presentation.ui.main.home.search

import com.climus.climeet.data.model.response.ClimberDetailInfoItem
import com.climus.climeet.presentation.ui.main.home.search.model.FollowClimber

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