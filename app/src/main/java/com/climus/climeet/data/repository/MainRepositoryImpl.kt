package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestRouteSimpleResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.MainApi
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi
) : MainRepository {

    override suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse> = runRemote { api.uploadImage(image) }

    override suspend fun searchGym(gymName: String)
    : BaseState<List<SearchGymResponse>> = runRemote { api.searchGym(gymName) }

    override suspend fun findBannerListBetweenDates(@Header("Authorization") accessToken : String)
    : BaseState<List<BannerDetailInfoResponse>> = runRemote { api.findBannerListBetweenDates(accessToken) }

    override suspend fun findClimberRankingOrderClearCount(@Header("Authorization") accessToken : String)
    : BaseState<List<BestClearClimberSimpleResponse>> = runRemote { api.findClimberRankingOrderClearCount(accessToken) }

    override suspend fun findClimberRankingOrderTime(@Header("Authorization") accessToken : String)
    : BaseState<List<BestTimeClimberSimpleResponse>> = runRemote { api.findClimberRankingOrderTime(accessToken) }

    override suspend fun findClimberRankingOrderLevel(@Header("Authorization") accessToken : String)
    : BaseState<List<BestLevelCimberSimpleResponse>> = runRemote { api.findClimberRankingOrderLevel(accessToken) }

    override suspend fun findPopularShorts(
        @Header("Authorization") accessToken : String,
        @Query("page") page : Int,
        @Query("size") size : Int)
    : BaseState<List<ShortsSimpleResponse>> = runRemote { api.findPopularShorts(accessToken, page, size) }

    override suspend fun findGymRankingOrderFollowCount(@Header("Authorization") accessToken : String)
    : BaseState<List<BestFollowGymSimpleResponse>> = runRemote { api.findGymRankingOrderFollowCount(accessToken) }

    override suspend fun findRouteRankingOrderSelectionCount(@Header("Authorization") accessToken : String)
    : BaseState<List<BestRouteSimpleResponse>> = runRemote { api.findRouteRankingOrderSelectionCount(accessToken) }

}