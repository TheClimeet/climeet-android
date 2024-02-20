package com.climus.climeet.data.repository

import com.climus.climeet.data.local.ClimbingRecordDao
import com.climus.climeet.data.local.ClimbingRecordData
import com.climus.climeet.data.local.RouteRecordDao
import com.climus.climeet.data.local.RouteRecordData
import com.climus.climeet.data.model.BaseState
import com.climus.climeet.data.model.request.AddShortsCommentRequest
import com.climus.climeet.data.model.request.CreateGymProfileReviewRequest
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
import com.climus.climeet.data.model.response.GetGymListToFollowResponse
import com.climus.climeet.data.model.response.GetGymProfileResponse
import com.climus.climeet.data.model.response.GetGymProfileReviewResponse
import com.climus.climeet.data.model.response.GetGymRouteInfoResponse
import com.climus.climeet.data.model.response.GetGymSkillDistributionResponse
import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.GymCompleteBestClimberResponse
import com.climus.climeet.data.model.response.GymLevelBestClimberResponse
import com.climus.climeet.data.model.response.GymProfileTabInfoResponse
import com.climus.climeet.data.model.response.GymProfileTopInfoResponse
import com.climus.climeet.data.model.response.GymTimeBestClimberResponse
import com.climus.climeet.data.model.response.GymWeekStatsResponse
import com.climus.climeet.data.model.response.MyStatsMonthResponse
import com.climus.climeet.data.model.response.SearchAvailableGymResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.ShortsListResponse
import com.climus.climeet.data.model.response.ShortsMainCommentItem
import com.climus.climeet.data.model.response.ShortsMainCommentResponse
import com.climus.climeet.data.model.response.ShortsSubCommentResponse
import com.climus.climeet.data.model.response.ShortsUpdatedFollowResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import com.climus.climeet.data.model.response.UserFollowSimpleResponse
import com.climus.climeet.data.model.response.UserFollowerInfoResponse
import com.climus.climeet.data.model.response.UserFollowingInfoResponse
import com.climus.climeet.data.model.response.UserHomeGymDetailResponse
import com.climus.climeet.data.model.response.UserHomeGymSimpleResponse
import com.climus.climeet.data.model.response.UserProfileInfoResponse
import com.climus.climeet.data.model.runRemote
import com.climus.climeet.data.remote.MainApi
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: MainApi,
    private val climbingRecordDao: ClimbingRecordDao,
    private val routeRecordDao: RouteRecordDao
) : MainRepository {

    override suspend fun uploadFile(
        file: MultipartBody.Part
    ): BaseState<UploadImgResponse> = runRemote { api.uploadFile(file) }

    override suspend fun searchGym(
        gymName: String,
        page: Int,
        size: Int
    ): BaseState<SearchGymResponse> =
        runRemote { api.searchGym(gymName, page, size) }

    override suspend fun getUserFollowing(
        userId: Long,
        userCategory: String
    ): BaseState<List<UserFollowingInfoResponse>> =
        runRemote { api.getUserFollowing(userId, userCategory) }

    override suspend fun getUserFollowers(
        userId: Long,
        userCategory: String
    ): BaseState<List<UserFollowerInfoResponse>> =
        runRemote { api.getUserFollowers(userId, userCategory) }

    override suspend fun getRecentShorts(
        page: Int,
        size: Int,
        filter: Map<String, Long>
    ): BaseState<ShortsListResponse> =
        runRemote { api.getRecentShorts(page, size, filter) }

    override suspend fun getPopularShorts(
        page: Int,
        size: Int,
        filter: Map<String, Long>
    ): BaseState<ShortsListResponse> =
        runRemote { api.getPopularShorts(page, size, filter) }

    override suspend fun getGymsFollowing(): BaseState<List<UserHomeGymDetailResponse>> =
        runRemote { api.getGymsFollowing() }

    override suspend fun searchAvailableGym(
        gymName: String,
        page: Int,
        size: Int
    ): BaseState<SearchAvailableGymResponse> =
        runRemote { api.searchAvailableGym(gymName, page, size) }

    override suspend fun getUserProfile(): BaseState<UserProfileInfoResponse> =
        runRemote { api.getUserProfile() }

    override suspend fun getShortsUpdatedFollow(): BaseState<List<ShortsUpdatedFollowResponse>> =
        runRemote { api.getShortsUpdatedFollow() }

    override suspend fun getHomeGyms(): BaseState<List<UserHomeGymSimpleResponse>> =
        runRemote { api.getHomeGyms() }

    override suspend fun getClimberFollowing(): BaseState<List<UserFollowSimpleResponse>> =
        runRemote { api.getClimberFollowing() }

    override suspend fun followUser(
        followingUserId: Long
    ): BaseState<String> =
        runRemote { api.followUser(followingUserId) }

    override suspend fun unfollowUser(
        followingUserId: Long
    ): BaseState<String> =
        runRemote { api.unfollowUser(followingUserId) }

    override suspend fun getClimberSearchingList(
        page: Int,
        size: Int,
        climberName: String
    ): BaseState<ClimberDetailInfoResponse> =
        runRemote { api.getClimberSearchingList(page, size, climberName) }

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
    ): BaseState<GetGymFilteringKeyResponse> = runRemote {
        api.getGymFilteringKey(
            gymId
        )
    }

    override suspend fun getGymFilteringKeyTime(
        gymId: Long,
        timePoint: String
    ): BaseState<GetGymFilteringKeyResponse> =
        runRemote { api.getGymFilteringKeyTime(gymId, timePoint) }

    override suspend fun getGymRouteInfoList(
        gymId: Long,
        body: GetGymRouteInfoRequest
    ): BaseState<GetGymRouteInfoResponse> = runRemote {
        api.getGymRouteInfoList(gymId, body)
    }

    override suspend fun uploadShorts(
        video: MultipartBody.Part?,
        body: ShortsDetailRequest
    ): BaseState<Unit> = runRemote {
        api.uploadShorts(video, body)
    }

    override suspend fun createTimerClimbingRecord(
        body: CreateTimerClimbingRecordRequest
    ): BaseState<ResponseBody> = runRemote { api.createTimerClimbingRecord(body) }

    override suspend fun getGymProfile(gymId: Long): BaseState<GetGymProfileResponse> =
        runRemote { api.getGymProfile(gymId) }


    override suspend fun patchBookMark(shortsId: Long): BaseState<Unit> =
        runRemote { api.patchBookMarks(shortsId) }

    override suspend fun patchFavorite(shortsId: Long): BaseState<Unit> =
        runRemote { api.patchFavorites(shortsId) }

    override suspend fun addShortsComment(
        shortsId: Long,
        filter: Map<String, Long>,
        body: AddShortsCommentRequest
    ): BaseState<ShortsMainCommentItem> = runRemote { api.addShortsComment(shortsId, filter, body) }

    override suspend fun getShortsCommentList(
        shortsId: Long,
        page: Int,
        size: Int
    ): BaseState<ShortsMainCommentResponse> =
        runRemote { api.getShortsCommentList(shortsId, page, size) }

    override suspend fun getShortsSubCommentList(
        shortsId: Long,
        parentCommentId: Long,
        page: Int,
        size: Int
    ): BaseState<ShortsSubCommentResponse> =
        runRemote { api.getShortsSubCommentList(shortsId, parentCommentId, page, size) }

    override suspend fun patchShortsCommentInteraction(
        shortsCommentId: Long,
        isLike: Boolean,
        isDislike: Boolean
    ): BaseState<String> =
        runRemote { api.patchShortsCommentInteraction(shortsCommentId, isLike, isDislike) }

    override suspend fun getGymProfileTopInfo(
        gymId: Long
    ): BaseState<GymProfileTopInfoResponse> = runRemote { api.getGymProfileTopInfo(gymId) }

    override suspend fun getGymProfileTabInfo(
        gymId: Long
    ): BaseState<GymProfileTabInfoResponse> = runRemote { api.getGymProfileTabInfo(gymId) }

    override suspend fun getMyStatsMonth(
        year: Int,
        month: Int
    ): BaseState<MyStatsMonthResponse> = runRemote { api.getMyStatsMonth(year, month) }

    override suspend fun getGymReview(
        gymId: Long,
        page: Int,
        size: Int
    ): BaseState<GetGymProfileReviewResponse> = runRemote { api.getGymReview(gymId, page, size) }

    override suspend fun createGymReview(
        gymId: Long,
        body: CreateGymProfileReviewRequest
    ): BaseState<ResponseBody> = runRemote { api.createGymReview(gymId, body) }

    override suspend fun getGymClimberRankingOrderClearCount(
        gymId: Long
    ): BaseState<List<GymCompleteBestClimberResponse>> =
        runRemote { api.getGymClimberRankingOrderClearCount(gymId) }

    override suspend fun getGymClimberRankingOrderLevel(
        gymId: Long
    ): BaseState<List<GymLevelBestClimberResponse>> =
        runRemote { api.getGymClimberRankingOrderLevel(gymId) }

    override suspend fun getGymClimberRankingOrderTime(
        gymId: Long
    ): BaseState<List<GymTimeBestClimberResponse>> =
        runRemote { api.getGymClimberRankingOrderTime(gymId) }

    override suspend fun getGymSkillDistribution(
        gymId: Long
    ): BaseState<List<GetGymSkillDistributionResponse>> =
        runRemote { api.getGymSkillDistribution(gymId) }

    override suspend fun getMyGymSkill(gymId: Long): BaseState<ResponseBody> =
        runRemote { api.getMyGymSkill(gymId) }

    override suspend fun getGymStatsWeek(gymId: Long): BaseState<GymWeekStatsResponse> =
        runRemote { api.getGymStatsWeek(gymId) }


    // -------- RoomDB ClimbingRecordDa0 암장 정보 -----------
    override fun insert(climbingRecordData: ClimbingRecordData) {
        climbingRecordDao.insert(climbingRecordData)
    }

    override fun update(climbingRecordData: ClimbingRecordData) {
        climbingRecordDao.update(climbingRecordData)
    }

    override fun delete(climbingRecordData: ClimbingRecordData) {
        climbingRecordDao.delete(climbingRecordData)
    }

    override fun deleteAllRecord() {
        climbingRecordDao.deleteAllRecord()
    }

    override fun getAllRecord(): List<ClimbingRecordData> {
        return climbingRecordDao.getAllRecord()
    }

    override fun getRecord(id: Int): ClimbingRecordData {
        return climbingRecordDao.getRecord(id)
    }

    // --------- RoomDB RouteRecordDao 루트 기록 --------------
    override fun insert(routeRecord: RouteRecordData) {
        routeRecordDao.insert(routeRecord)
    }

    override fun update(routeRecord: RouteRecordData) {
        routeRecordDao.update(routeRecord)
    }

    override fun delete(routeRecord: RouteRecordData) {
        routeRecordDao.delete(routeRecord)
    }

    override fun deleteAllRoute() {
        routeRecordDao.deleteAllRoute()
    }

    override fun deleteById(id: Int) {
        routeRecordDao.deleteById(id)
    }

    override fun getAllRoute(): List<RouteRecordData> {
        return routeRecordDao.getAllRoute()
    }

    override fun getRouteById(id: Int): RouteRecordData {
        return routeRecordDao.getRouteById(id)
    }

    override fun findExistRoute(sectorId: Long, routeId: Long): RouteRecordData? {
        return routeRecordDao.findExistRoute(sectorId, routeId)
    }

    override fun getAverageDifficultyOfCompleted(): Double {
        return routeRecordDao.getAverageDifficultyOfCompleted()
    }

    override fun getAllLevelRecord(): List<RouteRecordData> {
        return routeRecordDao.getAllLevelRecord()
    }

    override fun getSuccessCount(level: String): Int {
        return routeRecordDao.getSuccessCount(level)
    }

    override fun getAttemptCount(level: String): Int {
        return routeRecordDao.getAttemptCount(level)
    }

    override suspend fun followGym(gymId: Long): BaseState<String> =
        runRemote { api.followGym(gymId) }

    override suspend fun unFollowGym(gymId: Long): BaseState<String> =
        runRemote { api.unfollowGym(gymId) }

    override suspend fun getGymListToFollow(
        gymname: String,
        page: Int,
        size: Int
    ): BaseState<GetGymListToFollowResponse> = runRemote {
        api.getGymListToFollow(gymname, page, size)
    }
}