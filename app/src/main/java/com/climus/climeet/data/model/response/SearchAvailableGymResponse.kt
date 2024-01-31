package com.climus.climeet.data.model.response

data class SearchAvailableGymResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<SearchAvailableGymItem>
)

data class SearchAvailableGymItem(
    val id: Long,
    val name: String,
    val managerId: Long,
    val follower: Int,
    val profileImageUrl: String
)