package com.climus.climeet.presentation.ui.main.home.searchcrag

import com.climus.climeet.data.model.response.ClimberDetailInfoItem
import com.climus.climeet.data.model.response.SearchAvailableGymItem
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.ui.main.home.searchcrag.model.FollowClimber

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