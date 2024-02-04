package com.climus.climeet.presentation.ui.intro.signup.climer

import com.climus.climeet.data.model.response.SearchAvailableGymItem
import com.climus.climeet.data.model.response.SearchGymItem
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