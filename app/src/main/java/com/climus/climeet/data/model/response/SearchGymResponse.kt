package com.climus.climeet.data.model.response



data class SearchGymResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<SearchGymItem>
)

data class SearchGymItem(
    val id: Long,
    val name: String,
    val managerId: Long
)
