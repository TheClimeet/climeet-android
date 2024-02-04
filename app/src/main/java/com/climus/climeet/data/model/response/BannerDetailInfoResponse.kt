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
    val bannerTargetUrl : Int,

    @SerializedName("bannerStartDate")
    val bannerStartDate : LocalDate,

    @SerializedName("bannerEndDate")
    val bannerEndDate : LocalDate,

    @SerializedName("isPopup")
    val isPopup : Boolean,

    @SerializedName("linkUrl")
    val linkUrl : String
)
