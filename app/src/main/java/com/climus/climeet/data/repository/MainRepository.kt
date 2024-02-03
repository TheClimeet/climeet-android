package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody

interface MainRepository {

    suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse>

    suspend fun searchGym(
        gymName: String,
        page: Int,
        size: Int
    ): BaseState<SearchGymResponse>

    suspend fun searchAvailableGym(
        gymName: String,
        page: Int,
        size: Int
    ): BaseState<SearchAvailableGymResponse>

    suspend fun getRecentShorts(
        page: Int,
        size: Int
    ): BaseState<ShortsListResponse>

    suspend fun getPopularShorts(
        page: Int,
        size: Int
    ): BaseState<ShortsListResponse>

    suspend fun getShortsUpdatedFollow(): BaseState<List<ShortsUpdatedFollowResponse>>

}