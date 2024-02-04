package com.climus.climeet.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class BannerDetailInfoResponse(
    @SerializedName("id")
    val id : Long,

    @SerializedName("bannerImageUrl")
    val bannerImageUrl : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("bannerTargetUrl")
    val bannerTargetUrl : String,

    @SerializedName("bannerStartDate")
    val bannerStartDate : String,

    @SerializedName("bannerEndDate")
    val bannerEndDate : String,

    @SerializedName("isPopup")
    val isPopup : Boolean,

    @SerializedName("linkUrl")
    val linkUrl : String
)
