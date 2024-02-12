package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.CreateTimerClimbingRecordRequest
import com.climus.climeet.data.model.request.GetGymRouteInfoRequest
import com.climus.climeet.data.model.request.ShortsDetailRequest
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestRecordGymDetailInfoResponse
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.GetGymFilteringKeyResponse
import com.climus.climeet.data.model.response.GetGymProfileResponse
import com.climus.climeet.data.model.response.GetGymRouteInfoResponse
import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody

interface MainRepository {

    suspend fun uploadFile(
        file: MultipartBody.Part
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

    suspend fun findBannerListBetweenDates(): BaseState<List<BannerDetailInfoResponse>>

    suspend fun findClimberRankingOrderClearCount(): BaseState<List<BestClearClimberSimpleResponse>>

    suspend fun findClimberRankingOrderTime(): BaseState<List<BestTimeClimberSimpleResponse>>

    suspend fun findClimberRankingOrderLevel(): BaseState<List<BestLevelCimberSimpleResponse>>

    suspend fun findGymRankingOrderFollowCount(): BaseState<List<BestFollowGymSimpleResponse>>

    suspend fun findGymRankingListOrderSelectionCount(): BaseState<List<BestRecordGymDetailInfoResponse>>

    suspend fun findRouteRankingOrderSelectionCount(): BaseState<List<BestRouteDetailInfoResponse>>


    suspend fun getSelectDateRecord(
        startDate: String,
        endDate: String
    ): BaseState<List<GetSelectDateRecordResponse>>

    suspend fun getGymFilteringKey(
        gymId: Long,
    ): BaseState<GetGymFilteringKeyResponse>

    suspend fun getGymRouteInfoList(
        gymId: Long,
        body: GetGymRouteInfoRequest
    ): BaseState<GetGymRouteInfoResponse>

    suspend fun createTimerClimbingRecord(
        body: CreateTimerClimbingRecordRequest
    ): BaseState<String>

    suspend fun getGymProfile(
        gymId: Long
    ): BaseState<GetGymProfileResponse>

    suspend fun uploadShorts(
        video: MultipartBody.Part?,
        body: ShortsDetailRequest
    ): BaseState<Unit>

    suspend fun patchBookMark(
        shortsId: Long
    ): BaseState<Unit>

    suspend fun patchFavorite(
        shortsId: Long
    ): BaseState<Unit>
}