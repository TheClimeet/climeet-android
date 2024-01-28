package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.SearchGymResponse
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

    override suspend fun findClimberRankingOrderClearCount(): BaseState<List<BestClearClimberSimpleResponse>> = runRemote { api.findClimberRankingOrderClearCount() }

    override suspend fun findClimberRankingOrderTime(): BaseState<List<BestTimeClimberSimpleResponse>> = runRemote { api.findClimberRankingOrderTime() }

    override suspend fun findClimberRankingOrderLevel(): BaseState<List<BestLevelCimberSimpleResponse>> = runRemote { api.findClimberRankingOrderLevel() }

}