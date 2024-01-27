package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.MainApi
import okhttp3.MultipartBody
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi
) : MainRepository {

    override suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse> = runRemote { api.uploadImage(image) }

    override suspend fun searchGym(gymName: String): BaseState<List<SearchGymResponse>> = runRemote { api.searchGym(gymName) }

    override suspend fun getRecentShorts(page: Int, size: Int): BaseState<ShortsListResponse> = runRemote { api.getRecentShorts(page, size) }

    override suspend fun getPopularShorts(page: Int, size: Int): BaseState<ShortsListResponse> = runRemote { api.getPopularShorts(page, size) }
}