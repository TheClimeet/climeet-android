package com.climus.climeet.presentation.ui.intro.signup.climer

import com.climus.climeet.data.model.response.SearchAvailableGymItem
import com.climus.climeet.data.model.response.UserHomeGymSimpleResponse
import com.climus.climeet.presentation.ui.intro.signup.climer.model.AuthFollowCrag
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag

fun SearchAvailableGymItem.toAuthFollowCrag(
    keyword: String,
    onFollowCrag: (Long) -> Unit,
    onUnFollowCrag: (Long) -> Unit
) = AuthFollowCrag(
    id = id,
    imgUrl = profileImageUrl,
    followers = follower,
    name = name,
    keyword = keyword,
    onFollowCancelListener = onUnFollowCrag,
    onFollowListener = onFollowCrag
)

fun SearchAvailableGymItem.toFollowCrag(
    keyword: String,
)= FollowCrag(
    id = id,
    imgUrl = profileImageUrl,
    name = name,
    keyword = keyword,
    followers = follower
)

fun UserHomeGymSimpleResponse.toFollowCrag(
    keyword: String,
) = FollowCrag(
    id = gymId,
    imgUrl = gymProfileUrl,
    name = gymName,
    keyword = keyword,
    followers = followerCount,
    isFollowing = true
)