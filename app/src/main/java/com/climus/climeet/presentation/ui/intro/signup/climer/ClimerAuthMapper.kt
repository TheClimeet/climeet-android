package com.climus.climeet.presentation.ui.intro.signup.climer

import com.climus.climeet.data.model.response.SearchAvailableGymItem
import com.climus.climeet.data.model.response.SearchGymItem
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.data.model.response.UserHomeGymSimpleResponse
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.util.Constants

fun SearchAvailableGymItem.toFollowCrag(
    keyword: String,
) = FollowCrag(
    id = id,
    imgUrl = profileImageUrl,
    followers = follower,
    name = name,
    keyword = keyword,
)

fun UserHomeGymSimpleResponse.toFollowCrag(
    keyword: String,
) = FollowCrag(
    id = gymId,
    imgUrl = gymProfileUrl,
    name = gymName,
    keyword = keyword,
    followers = 0,
    isFollowing = true
)