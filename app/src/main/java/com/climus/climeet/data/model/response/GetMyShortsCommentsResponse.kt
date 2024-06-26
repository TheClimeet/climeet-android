package com.climus.climeet.data.model.response

data class GetMyShortsCommentsResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<MyShortsComments>
)

data class MyShortsComments(
    val commentId: Long,
    val profileImageUrl: String?,
    val content: String,
    val createdDate: String
)