package com.climus.climeet.data.repository

import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.CreateTimerClimbingRecordRequest
import com.climus.climeet.data.model.request.GetGymRouteInfoRequest
import com.climus.climeet.data.model.response.BannerDetailInfoResponse
import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestFollowGymSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestRecordGymDetailInfoResponse
import com.climus.climeet.data.model.response.BestRouteDetailInfoResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.GetGymFilteringKeyResponse
import com.climus.climeet.data.model.response.GetGymRouteInfoResponse
import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsSimpleResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.MainApi
import okhttp3.MultipartBody
import retrofit2.http.Query
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi
) : MainRepository {

    override suspend fun uploadImage(
        image: MultipartBody.Part
    ): BaseState<UploadImgResponse> = runRemote { api.uploadImage(image) }

    override suspend fun searchGym(
        gymName: String,
        page: Int,
        size: Int
    ): BaseState<SearchGymResponse> =
        runRemote { api.searchGym(gymName, page, size) }

    override suspend fun getRecentShorts(page: Int, size: Int): BaseState<ShortsListResponse> =
        runRemote { api.getRecentShorts(page, size) }

    override suspend fun getPopularShorts(page: Int, size: Int): BaseState<ShortsListResponse> =
        runRemote { api.getPopularShorts(page, size) }

    override suspend fun searchAvailableGym(
        gymName: String,
        page: Int,
        size: Int
    ): BaseState<SearchAvailableGymResponse> =
        runRemote { api.searchAvailableGym(gymName, page, size) }

    override suspend fun getShortsUpdatedFollow(): BaseState<List<ShortsUpdatedFollowResponse>> =
        runRemote { api.getShortsUpdatedFollow() }


    override suspend fun findBannerListBetweenDates()
            : BaseState<List<BannerDetailInfoResponse>> =
        runRemote { api.findBannerListBetweenDates() }

    override suspend fun findClimberRankingOrderClearCount()
            : BaseState<List<BestClearClimberSimpleResponse>> =
        runRemote { api.findClimberRankingOrderClearCount() }

    override suspend fun findClimberRankingOrderTime()
            : BaseState<List<BestTimeClimberSimpleResponse>> =
        runRemote { api.findClimberRankingOrderTime() }

    override suspend fun findClimberRankingOrderLevel()
            : BaseState<List<BestLevelCimberSimpleResponse>> =
        runRemote { api.findClimberRankingOrderLevel() }

    override suspend fun findGymRankingOrderFollowCount()
            : BaseState<List<BestFollowGymSimpleResponse>> =
        runRemote { api.findGymRankingOrderFollowCount() }

    override suspend fun findGymRankingListOrderSelectionCount()
            : BaseState<List<BestRecordGymDetailInfoResponse>> =
        runRemote { api.findGymRankingListOrderSelectionCount() }

    override suspend fun findRouteRankingOrderSelectionCount()
            : BaseState<List<BestRouteDetailInfoResponse>> =
        runRemote { api.findRouteRankingOrderSelectionCount() }

    override suspend fun getSelectDateRecord(
        startDate: String,
        endDate: String
    ): BaseState<List<GetSelectDateRecordResponse>> =
        runRemote { api.getSelectDateRecord(startDate, endDate) }

    override suspend fun getGymFilteringKey(
        gymId: Long,
    ): BaseState<GetGymFilteringKeyResponse> = runRemote { api.getGymFilteringKey(
        gymId
    ) }

    override suspend fun getGymRouteInfoList(
        gymId: Long,
        body: GetGymRouteInfoRequest
    ): BaseState<GetGymRouteInfoResponse> = runRemote {
        api.getGymRouteInfoList(gymId, body)
    }

    override suspend fun createTimerClimbingRecord(
        body: CreateTimerClimbingRecordRequest
    ): BaseState<String> = runRemote { api.createTimerClimbingRecord(body) }

}