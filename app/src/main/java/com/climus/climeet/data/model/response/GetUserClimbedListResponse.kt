package com.climus.climeet.data.model.response

data class GetUserClimbedListResponse(
    val visitedClimbingGym : List<UserClimbedGym>
)

data class UserClimbedGym(
    val userId : Int,
    val gymName : String
)
