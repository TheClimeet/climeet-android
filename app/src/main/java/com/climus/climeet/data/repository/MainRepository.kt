package com.climus.climeet.data.repository

import com.climus.climeet.data.local.ClimbingRecordData
import com.climus.climeet.data.local.RouteRecordData
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.AddShortsCommentRequest
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
import com.climus.climeet.data.model.response.ClimberDetailInfoResponse
import com.climus.climeet.data.model.response.GetGymFilteringKeyResponse
import com.climus.climeet.data.model.response.GetGymProfileResponse
import com.climus.climeet.data.model.response.GetGymRouteInfoResponse
import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.GymProfileTabInfoResponse
import com.climus.climeet.data.model.response.GymProfileTopInfoResponse
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsMainCommentItem
import com.climus.climeet.data.model.response.ShortsMainCommentResponse
import com.climus.climeet.data.model.response.ShortsSubCommentResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.data.model.response.UserHomeGymSimpleResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Path
import retrofit2.http.Query

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
        size: Int,
        filter: Map<String, Long>
    ): BaseState<ShortsListResponse>

    suspend fun getPopularShorts(
        page: Int,
        size: Int,
        filter: Map<String, Long>
    ): BaseState<ShortsListResponse>

    suspend fun getShortsUpdatedFollow(): BaseState<List<ShortsUpdatedFollowResponse>>

    suspend fun findBannerListBetweenDates(): BaseState<List<BannerDetailInfoResponse>>

    suspend fun findClimberRankingOrderClearCount(): BaseState<List<BestClearClimberSimpleResponse>>

    suspend fun findClimberRankingOrderTime(): BaseState<List<BestTimeClimberSimpleResponse>>

    suspend fun findClimberRankingOrderLevel(): BaseState<List<BestLevelCimberSimpleResponse>>

    suspend fun findGymRankingOrderFollowCount(): BaseState<List<BestFollowGymSimpleResponse>>

    suspend fun findGymRankingListOrderSelectionCount(): BaseState<List<BestRecordGymDetailInfoResponse>>

    suspend fun findRouteRankingOrderSelectionCount(): BaseState<List<BestRouteDetailInfoResponse>>

    suspend fun getClimberFollowing(): BaseState<List<UserFollowSimpleResponse>>

    suspend fun getHomeGyms(): BaseState<List<UserHomeGymSimpleResponse>>

    suspend fun getClimberSearchingList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("climberName") climberName: String
    ): BaseState<ClimberDetailInfoResponse>

    suspend fun getSelectDateRecord(
        startDate: String,
        endDate: String
    ): BaseState<List<GetSelectDateRecordResponse>>

    suspend fun followUser(
        @Path("followingUserId") followingUserId: Long
    ): BaseState<String>

    suspend fun unfollowUser(
        @Path("followingUserId") followingUserId: Long
    ): BaseState<String>

    suspend fun getGymFilteringKey(
        gymId: Long,
    ): BaseState<GetGymFilteringKeyResponse>

    suspend fun getGymRouteInfoList(
        gymId: Long,
        body: GetGymRouteInfoRequest
    ): BaseState<GetGymRouteInfoResponse>

    suspend fun createTimerClimbingRecord(
        body: CreateTimerClimbingRecordRequest
    ): BaseState<ResponseBody>

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

    suspend fun getShortsSubCommentList(
        shortsId: Long,
        parentCommentId: Long,
        page: Int,
        size: Int
    ): BaseState<ShortsSubCommentResponse>

    suspend fun getShortsCommentList(
        shortsId: Long,
        page: Int,
        size: Int
    ): BaseState<ShortsMainCommentResponse>

    suspend fun addShortsComment(
        shortsId: Long,
        filter: Map<String, Long>,
        body: AddShortsCommentRequest
    ): BaseState<ShortsMainCommentItem>

    suspend fun patchShortsCommentInteraction(
        shortsCommentId: Long,
        isLike: Boolean,
        isDislike: Boolean
    ): BaseState<String>

    suspend fun getGymProfileTopInfo(
        gymId: Long
    ): BaseState<GymProfileTopInfoResponse>

    suspend fun getGymProfileTabInfo(
        gymId: Long
    ): BaseState<GymProfileTabInfoResponse>

    // -------- RoomDB ClimbingRecordDao 암장 정보 ----------
    fun insert(climbingRecordData: ClimbingRecordData)
    fun update(climbingRecordData: ClimbingRecordData)
    fun delete(climbingRecordData: ClimbingRecordData)
    fun deleteAllRecord()
    fun getAllRecord(): List<ClimbingRecordData>
    fun getRecord(id: Int): ClimbingRecordData

    // ------ RoomDB RouteRecordDao 루트 기록 정보 ---------
    fun insert(routeRecord: RouteRecordData)

    fun update(routeRecord: RouteRecordData)

    fun delete(routeRecord: RouteRecordData)

    fun deleteAllRoute()

    fun deleteById(id: Int)

    fun getAllRoute(): List<RouteRecordData>

    fun getRouteById(id: Int): RouteRecordData

    fun findExistRoute(sectorId: Long, routeId: Long): RouteRecordData?

    fun getAverageDifficultyOfCompleted(): Double

    fun getAllLevelRecord(): List<RouteRecordData>

    fun getSuccessCount(level: String): Int

    fun getAttemptCount(level: String): Int
}