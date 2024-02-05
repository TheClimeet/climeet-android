package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.BestRouteSimpleResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Query

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

    suspend fun findBannerListBetweenDates(@Header("Authorization") accessToken : String): BaseState<List<BannerDetailInfoResponse>>

    suspend fun findClimberRankingOrderClearCount(@Header("Authorization") accessToken : String): BaseState<List<BestClearClimberSimpleResponse>>

    suspend fun findClimberRankingOrderTime(@Header("Authorization") accessToken : String): BaseState<List<BestTimeClimberSimpleResponse>>

    suspend fun findClimberRankingOrderLevel(@Header("Authorization") accessToken : String): BaseState<List<BestLevelCimberSimpleResponse>>

    suspend fun findPopularShorts(
        @Query("page") page : Int,
        @Query("size") size : Int
    ): BaseState<List<ShortsSimpleResponse>>

    suspend fun findGymRankingOrderFollowCount() : BaseState<List<BestFollowGymSimpleResponse>>

    suspend fun findRouteRankingOrderSelectionCount(@Header("Authorization") accessToken : String) : BaseState<List<BestRouteDetailInfoResponse>>


    suspend fun getSelectDateRecord(
        startDate: String,
        endDate: String
    ): BaseState<List<GetSelectDateRecordResponse>>

}