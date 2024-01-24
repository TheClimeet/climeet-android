package com.climus.climeet.presentation.ui.intro.signup.climer

import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.presentation.ui.intro.signup.climer.model.FollowCrag
import com.climus.climeet.presentation.util.Constants

fun SearchGymResponse.toFollowCrag(
    keyword: String,
) = FollowCrag(
    id = id,
    imgUrl = Constants.TEST_IMG,
    name = name,
    keyword = keyword,
)