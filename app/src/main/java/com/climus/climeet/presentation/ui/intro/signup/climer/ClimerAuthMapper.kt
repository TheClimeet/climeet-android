package com.climus.climeet.presentation.ui.intro.signup.climer

import com.climus.climeet.data.model.response.SearchGymItem
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.util.Constants

fun SearchGymItem.toFollowCrag(
    keyword: String,
) = FollowCrag(
    id = id,
    imgUrl = Constants.TEST_IMG,
    name = name,
    keyword = keyword,
)