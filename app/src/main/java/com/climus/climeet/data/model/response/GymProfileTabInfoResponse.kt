package com.climus.climeet.data.model.response

data class GymProfileTabInfoResponse (
    val gymId: Long,
    val address: String?,
    val location: String?,
    val tel: String?,
    val businessHours: Map<String, List<String>>?,
    val serviceList: List<String>?,
    val priceList: Map<String, String>?
)