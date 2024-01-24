package com.climus.climeet.data.model

data class ErrorResponse(
    val isSuccess: String,
    val code: String,
    val message: String
)
