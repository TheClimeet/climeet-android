package com.climus.climeet.presentation.ui.intro.signup.admin.model

data class SearchCragUiData(
    val id: Long = -1,
    val imgUrl: String = "",
    val name: String = "",
    val keyword: String = "",
    val onClickListener: (Long, String, String) -> Unit
)
