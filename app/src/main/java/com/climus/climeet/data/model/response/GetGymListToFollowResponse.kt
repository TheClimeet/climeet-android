package com.climus.climeet.data.model.response

data class GetGymListToFollowResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<GymListToFollowItem>
)


data class GymListToFollowItem(
    val gymId: Long,
    val name: String,
    val managerId: Long,
    val follower: Int,
    val profileImageUrl: String,
    val isFollow: Boolean
)
