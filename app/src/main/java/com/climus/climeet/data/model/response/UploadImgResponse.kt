package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName

data class UploadImgResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: ImgResult,
) {
    data class ImgResult(
        @SerializedName("imgUrl")
        val imgUrl: String
    )
}
