package com.climus.climeet.presentation.ui.intro.signup.admin.model

data class SearchCragUiData(
    val id: Long,
    val img: String = "",
    val name: String = "",
    val keyword: String = "",
    val onClickListener: (Long) -> Unit
)
