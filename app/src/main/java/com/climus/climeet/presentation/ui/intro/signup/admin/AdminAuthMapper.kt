package com.climus.climeet.presentation.ui.intro.signup.admin

import com.climus.climeet.data.model.response.SearchGymItem
import com.climus.climeet.presentation.ui.intro.signup.admin.model.SearchCragUiData
import com.climus.climeet.presentation.util.Constants.TEST_IMG


fun SearchGymItem.toSearchCragUiData(
    keyword: String,
    onClickListener: (Long, String, String) -> Unit
) = SearchCragUiData(
    id = id,
    imgUrl = TEST_IMG,
    name = name,
    keyword = keyword,
    onClickListener = onClickListener
)