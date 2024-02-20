package com.climus.climeet.data.remote

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
import com.climus.climeet.data.model.response.GetGymProfileResponse
import com.climus.climeet.data.model.response.GetGymProfileReviewResponse
import com.climus.climeet.data.model.response.GetGymRouteInfoResponse
import com.climus.climeet.data.model.response.GetSelectDateRecordResponse
import com.climus.climeet.data.model.response.GymCompleteBestClimberResponse
import com.climus.climeet.data.model.response.GymLevelBestClimberResponse
import com.climus.climeet.data.model.response.GymProfileTabInfoResponse
import com.climus.climeet.data.model.response.GymProfileTopInfoResponse
import com.climus.climeet.data.model.response.GymTimeBestClimberResponse
import com.climus.climeet.data.model.response.MyStatsMonthResponse
import com.climus.climeet.data.model.response.RefreshTokenResponse
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
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MainApi {

    @Multipart
    @POST("api/file")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Response<UploadImgResponse>

    @GET("api/gyms/search/all")
    suspend fun searchGym(
        @Query("gymname") gymName: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchGymResponse>

    @GET("api/gyms/search")
    suspend fun searchAvailableGym(
        @Query("gymname") gymName: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchAvailableGymResponse>

    @GET("/api/climber-following")
    suspend fun getClimberFollowing(
    ): Response<List<UserFollowSimpleResponse>>

    @POST("refresh-token")
    suspend fun refreshToken(
        @Query("refreshToken") refreshToken: String
    ): Response<RefreshTokenResponse>

    @GET("api/shorts/latest")
    suspend fun getRecentShorts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @QueryMap filter: Map<String, Long>,
    ): Response<ShortsListResponse>

    @POST("/follow-relationship/{userId}")
    suspend fun followUser(
        @Path("userId") userId: Long
    ): Response<String>

    @DELETE("/follow-relationship/{userId}")
    suspend fun unfollowUser(
        @Path("userId") userId: Long
    ): Response<String>

    @GET("/api/climber/search")
    suspend fun getClimberSearchingList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("climberName") climberName: String
    ): Response<ClimberDetailInfoResponse>

    @GET("api/shorts/popular")
    suspend fun getPopularShorts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @QueryMap filter: Map<String, Long>,
    ): Response<ShortsListResponse>

    @GET("api/shorts/profile")
    suspend fun getShortsUpdatedFollow(): Response<List<ShortsUpdatedFollowResponse>>

    @GET("/api/climbing-records/between-dates")
    suspend fun getSelectDateRecord(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<GetSelectDateRecordResponse>>

    @GET("/api/banners")
    suspend fun findBannerListBetweenDates(): Response<List<BannerDetailInfoResponse>>

    @GET("/api/home/rank/weeks/climbers/clear")
    suspend fun findClimberRankingOrderClearCount(): Response<List<BestClearClimberSimpleResponse>>

    @GET("/api/home/rank/weeks/climbers/time")
    suspend fun findClimberRankingOrderTime(): Response<List<BestTimeClimberSimpleResponse>>

    @GET("/api/home/rank/weeks/climbers/level")
    suspend fun findClimberRankingOrderLevel(): Response<List<BestLevelCimberSimpleResponse>>

    @GET("/api/home/homegyms")
    suspend fun getHomeGyms(): Response<List<UserHomeGymSimpleResponse>>

    @GET("/api/home/rank/weeks/gyms/follow")
    suspend fun findGymRankingOrderFollowCount(
    ): Response<List<BestFollowGymSimpleResponse>>

    @GET("/api/home/rank/weeks/gyms/record")
    suspend fun findGymRankingListOrderSelectionCount(
    ): Response<List<BestRecordGymDetailInfoResponse>>

    @GET("/api/gym-following")
    suspend fun getGymsFollowing(
    ): Response<List<UserHomeGymDetailResponse>>

    @GET("/api/home/rank/weeks/routes")
    suspend fun findRouteRankingOrderSelectionCount(
    ): Response<List<BestRouteDetailInfoResponse>>

    @GET("/api/profile")
    suspend fun getUserProfile(
    ): Response<UserProfileInfoResponse>


    @GET("/api/gyms/{gymId}/version/key")
    suspend fun getGymFilteringKey(
        @Path("gymId") gymId: Long,
    ): Response<GetGymFilteringKeyResponse>

    @GET("/api/gyms/{gymId}/version/key")
    suspend fun getGymFilteringKeyTime(
        @Path("gymId") gymId: Long,
        @Query("timePoint") timePoint: String
    ): Response<GetGymFilteringKeyResponse>

    @POST("/api/gyms/{gymId}/version/route")
    suspend fun getGymRouteInfoList(
        @Path("gymId") gymId: Long,
        @Body params: GetGymRouteInfoRequest
    ): Response<GetGymRouteInfoResponse>

    @POST("/api/climbing-records")
    suspend fun createTimerClimbingRecord(
        @Body params: CreateTimerClimbingRecordRequest
    ): Response<ResponseBody>

    @GET("/api/gyms/{gymId}")
    suspend fun getGymProfile(
        @Path("gymId") gymId: Long
    ): Response<GetGymProfileResponse>

    @Multipart
    @POST("/api/shorts")
    suspend fun uploadShorts(
        @Part video: MultipartBody.Part?,
        @Part("createShortsRequest") createShortsRequest: ShortsDetailRequest
    ): Response<Unit>

    @PATCH("/api/shorts/{shortsId}/bookmarks")
    suspend fun patchBookMarks(
        @Path("shortsId") shortsId: Long
    ): Response<Unit>

    @PATCH("/api/Shorts/{shortsId}/likes")
    suspend fun patchFavorites(
        @Path("shortsId") shortsId: Long
    ): Response<Unit>

    @GET("/api/shorts/{shortsId}/{parentCommentId}")
    suspend fun getShortsSubCommentList(
        @Path("shortsId") shortsId: Long,
        @Path("parentCommentId") parentCommentId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ShortsSubCommentResponse>

    @GET("/api/shorts/{shortsId}/shortsComments")
    suspend fun getShortsCommentList(
        @Path("shortsId") shortsId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ShortsMainCommentResponse>

    @GET("/api/followees")
    suspend fun getUserFollowing(
        @Query("userId") userId: Long,
        @Query("userCategory") userCategory: String
    ): Response<List<UserFollowingInfoResponse>>

    @GET("/api/followers")
    suspend fun getUserFollowers(
        @Query("userId") userId: Long,
        @Query("userCategory") userCategory: String
    ): Response<List<UserFollowerInfoResponse>>

    @POST("/api/shorts/{shortsId}/shortsComments")
    suspend fun addShortsComment(
        @Path("shortsId") shortsId: Long,
        @QueryMap filter: Map<String, Long>,
        @Body params: AddShortsCommentRequest
    ): Response<ShortsMainCommentItem>

    @PATCH("/api/shortsComments/{shortsCommentId}")
    suspend fun patchShortsCommentInteraction(
        @Path("shortsCommentId") shortsCommentId: Long,
        @Query("isLike") isLike: Boolean,
        @Query("isDislike") isDislike: Boolean
    ): Response<String>

    @GET("/api/gyms/{gymId}")
    suspend fun getGymProfileTopInfo(
        @Path("gymId") gymId: Long
    ): Response<GymProfileTopInfoResponse>

    @GET("/api/gyms/{gymId}/tab")
    suspend fun getGymProfileTabInfo(
        @Path("gymId") gymId: Long
    ): Response<GymProfileTabInfoResponse>

    @GET("/api/climbing-records/users/statistics/months")
    suspend fun getMyStatsMonth(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<MyStatsMonthResponse>

    @GET("/api/gyms/{gymId}/review")
    suspend fun getGymReview(
        @Path("gymId") gymID: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<GetGymProfileReviewResponse>

    @POST("/api/gyms/{gymId}/review")
    suspend fun createGymReview(
        @Path("gymId") gymID: Long,
        @Body params: CreateGymProfileReviewRequest
    ): Response<ResponseBody>

    @GET("/api/climbing-records/gyms/{gymId}/rank/weeks/climbers/clear")
    suspend fun getGymClimberRankingOrderClearCount(
        @Path("gymId") gymID: Long,
    ): Response<List<GymCompleteBestClimberResponse>>

    @GET("/api/climbing-records/gyms/{gymId}/rank/weeks/climbers/level")
    suspend fun getGymClimberRankingOrderLevel(
        @Path("gymId") gymID: Long,
    ): Response<List<GymLevelBestClimberResponse>>

    @GET("/api/climbing-records/gyms/{gymId}/rank/weeks/climbers/time")
    suspend fun getGymClimberRankingOrderTime(
        @Path("gymId") gymID: Long,
    ): Response<List<GymTimeBestClimberResponse>>


}