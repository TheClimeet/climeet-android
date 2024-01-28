package com.climus.climeet.data.remote

import com.climus.climeet.data.model.response.BestClearClimberSimpleResponse
import com.climus.climeet.data.model.response.BestLevelCimberSimpleResponse
import com.climus.climeet.data.model.response.BestTimeClimberSimpleResponse
import com.climus.climeet.data.model.response.SearchGymResponse
import com.climus.climeet.data.model.response.UploadImgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MainApi {

    @Multipart
    @POST("/file")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<UploadImgResponse>

    @GET("/gym/search/all")
    suspend fun searchGym(
        @Query("gymname") gymName: String
    ): Response<List<SearchGymResponse>>

    @GET("/api/rank/week/climber/clear")
    suspend fun findClimberRankingOrderClearCount(): Response<List<BestClearClimberSimpleResponse>>

    @GET("/api/rank/week/climber/time")
    suspend fun findClimberRankingOrderTime(): Response<List<BestTimeClimberSimpleResponse>>

    @GET("/api/rank/week/climber/level")
    suspend fun findClimberRankingOrderLevel(): Response<List<BestLevelCimberSimpleResponse>>

    @GET("/api/shorts/popular")
    suspend fun findPopularShorts(
        @Query("page") page : Int,
        @Query("size") size : Int
    ): Response<List<BestLevelCimberSimpleResponse>>

}