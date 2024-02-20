package com.climus.climeet.data.model.response

data class GetGymProfileReviewResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: Result
)

data class Result(
    val summary: Summary,
    val reviewList: List<Review>?
)

data class Summary(
    val gymId: Long,
    val averageRating: Float,
    val reviewCount: Int,
    val myReview: Review?
)

data class Review(
    val userId: Long,
    val profileImageUrl: String,
    val profileName: String,
    val rating: Float,
    val content: String,
    val updatedAt: String
)